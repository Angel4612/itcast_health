package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约套餐管理
 */
public interface OrderSettingDao {
    /**
     * 将预约设置的信息, 添加到数据库
     *
     * @param orderSetting
     */
    void add(OrderSetting orderSetting);

    /**
     * 更新预约设置的信息
     *
     * @param orderSetting
     */
    void editNumberByOrderDate(OrderSetting orderSetting);

    /**
     * 查找数据库中是否存在相同日期的预约信息
     *
     * @param date
     * @return
     */
    Long findCountByOrderDate(Date date);

    /**
     * 根据Map集合中指定的范围, 查询范围内的预约设置信息
     */
    List<OrderSetting> getOrderSettingByMonth(Map<String, String> dateRange);

    /**
     * 据指定日期和人数进行修改
     * @param orderSetting
     */
    void editNumberByDate(OrderSetting orderSetting);


}
