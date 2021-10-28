package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.service.ReportService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    /*

                    reportDate: 今天的日期

                    todayNewMember :今日新增会员数
                    totalMember :总会员数
                    thisWeekNewMember : 本周新增会员数
                    thisMonthNewMember : 本月新增会员数
                    todayOrderNumber : 今日预约人数
                    todayVisitsNumber :今日到诊人数
                    thisWeekOrderNumber : 本周预约人数
                    thisWeekVisitsNumber : 本周到诊人数
                    thisMonthOrderNumber : 本月预约人数
                    thisMonthVisitsNumber : 本月到诊人数
                    hotSetmeal :[  热门套餐
                        {name:'阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐',setmeal_count:200,proportion:0.222},
                        {name:'阳光爸妈升级肿瘤12项筛查体检套餐',setmeal_count:200,proportion:0.222}
                    ]


     */
    @Override
    public Map<String, Object> getBusinessReport() throws Exception {

        Map<String, Object> map = new HashMap<>();
        //获得当前日期
        String today = DateUtils.parseDate2String(DateUtils.getToday());

        // 获得本周一的日期
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        // 获得本月第一天的日期
        String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());

        // todayNewMember :今日新增会员数
        Integer memberCountByDate = memberDao.findMemberCountByDate(today);
        // 获取总会员数
        Integer totalMember = memberDao.findMemberTotalCount();
        // 获取本周新增会员数
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(thisWeekMonday);
        // 获取本月新增会员数
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);
        // 获取今日预约人数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(today);
        // 获取本周预约人数
        Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(thisWeekMonday);
        // 获取本月预约人数
        Integer thisMonthOrderNumber = orderDao.findOrderCountAfterDate(firstDay4ThisMonth);
        // 获取今日到诊人数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(today);
        // 获取本周到诊人数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(thisWeekMonday);
        // 获取本月到诊人数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDay4ThisMonth);
        // 获取热门套餐
        List<Map<String, Object>> hotSetmeal = orderDao.findHotSetmeal();
        // 获取集合中的

        map.put("reportDate", today);
        map.put("todayNewMember", memberCountByDate);
        map.put("totalMember",totalMember);
        map.put("thisWeekNewMember",thisWeekNewMember);
        map.put("thisMonthNewMember",thisMonthNewMember);
        map.put("todayOrderNumber",todayOrderNumber);
        map.put("thisWeekOrderNumber",thisWeekOrderNumber);
        map.put("thisMonthOrderNumber",thisMonthOrderNumber);
        map.put("todayVisitsNumber",todayVisitsNumber);
        map.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        map.put("hotSetmeal",hotSetmeal);
        return map;
    }
}
