package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void add(List<OrderSetting> list) {
        if (list != null && list.size() > 0) {
            // 遍历集合, 获取到每一个OrderSetting
            for (OrderSetting orderSetting : list) {
                // 检查此数据(日期)是否存在(select)
                Long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if (count > 0) {
                    // 如果已经存在, 执行更新操作(update)
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else {
                    // 如果不存在, 将OrderSetting对象中的信息添加到数据库(insert)
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    @Override
    public List<Map<String, Object>> getOrderSettingByMonth(String date) {
        // 拼接日期
        String beginDate = date + "-1";
        String endDate = date + "-31";
        // 将开始日期和结束日期存入map集合中
        HashMap<String, String> map = new HashMap<>();
        map.put("begin", beginDate);
        map.put("end", endDate);
        // 根据map中的数据, 调用DAO层, 查询指定日期范围内的预约设置数据
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);

        List<Map<String, Object>> data = new ArrayList<>();

        // 遍历集合, 获取到每一个预约设置对象, 对象中存放着预约日期, 可预约人数和已预约人数
        for (OrderSetting orderSetting : list) {
            // 将每一个对象封装到map集合中
            HashMap<String, Object> orderSettingMap = new HashMap<>();
            orderSettingMap.put("date",orderSetting.getOrderDate().getDate());//获得日期（几号）
            orderSettingMap.put("number",orderSetting.getNumber());//可预约人数
            orderSettingMap.put("reservations",orderSetting.getReservations());//已预约人数
            // 将map集合添加到list集合中, 用于返回
            data.add(orderSettingMap);
        }
        return data;
    }

    /**
     * 据指定日期和人数进行修改
     * @param orderSetting
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        // 先查询当前日期是否有数据
        Long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        // 如果有数据, 就更新数据
        if (count > 0) {
            orderSettingDao.editNumberByDate(orderSetting);
        }else {
            // 如果没有数据, 就添加数据
            orderSettingDao.add(orderSetting);
        }
    }
}
