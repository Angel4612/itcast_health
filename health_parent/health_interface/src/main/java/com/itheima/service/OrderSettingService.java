package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {

    /**
     * 将表格中预约管理的数据, 添加到数据库
     * @param list
     */
    void add(List<OrderSetting> list);

    /**
     * 根据日期, 查询预约设置的数据
     */
    List<Map<String, Object>> getOrderSettingByMonth(String date);

    /**
     * 据指定日期和人数进行修改
     * @param orderSetting
     */
    void editNumberByDate(OrderSetting orderSetting);



}
