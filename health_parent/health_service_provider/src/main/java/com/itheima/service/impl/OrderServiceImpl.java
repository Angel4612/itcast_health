package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.CheckItemService;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    public Result order(Map map) throws Exception {
        //1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        // 1.1根据map集合获取当前日期
        String orderDate = (String) map.get("orderDate");
        // 1.2通过工具类, 将当前日期字符串转换成Date类型
        Date date = DateUtils.parseString2Date(orderDate);
        // 1.3去数据库中查找当前日期的预约设置
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        // 1.4如果没有进行预约设置则提示预约失败
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }


        //2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        // 2.1 上面已经获取到orderSetting对象, 那么这里可以直接获取可预约人数
        int number = orderSetting.getNumber();
        // 2.2 再获取已预约人数
        int reservations = orderSetting.getReservations();
        // 2.3 如果已预约人数大于等于可预约人数, 则不能预约
        if (reservations >= number) {
            return new Result(false,MessageConstant.ORDER_FULL);
        }
        //3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
        // 3.1 检查当前用户是否为会员, 获取用户在移动端输入的手机号码
        String telephone = (String) map.get("telephone");
        // 3.2 去数据库根据这个手机号码, 查询是否存在对应的member
        Member member = memberDao.findByTelephone(telephone);
        // 3.3 如果member不为null, 说明已经存在这个会员
        if (member != null) {
            // 会员id
            Integer memberId = member.getId();
            // 套餐id
            Integer setmealId = Integer.parseInt((String) map.get("setmealId"));
            // order对象中需要存有会员id和套餐id
            Order order = new Order();
            // 3.3.1 查看这个会员的预约信息, 如果已经有预约信息了就提示不能重复预约
            List<Order> list = orderDao.findByCondition(order);
            // 如果会员id相同, 套餐id也相同, 说明已经预约过了
            if (list != null && list.size() > 0) {
                //已经完成了预约，不能重复预约
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }

        //5、预约成功，更新当日的已预约人数
        // 如果代码执行到这里, 说明不可以预约的情况都不存在, 那么则是可以预约
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        // 通过预约日期, 更新已预约人数(修改数据库)
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        //4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
        if (member == null) {
            member = new Member();
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }

        // 最后保存预约信息到预约表
        Order order = new Order(member.getId(),
                date,
                (String) map.get("orderType"),
                Order.ORDERSTATUS_NO,
                Integer.parseInt((String) map.get("setmealId")));

        orderDao.add(order);

        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    /**
     * 根据id查询体检人, 体检套餐, 体检日期, 预约类型
     */
    @Override
    public Map findById(Integer id) throws Exception {
        //  查询体检详情
        Map map = orderDao.findById4Detail(id);
        // 处理日期格式
        Date orderDate = (Date) map.get("orderDate");
        map.put("orderDate",DateUtils.parseDate2String(orderDate));
        return map;
    }
}
