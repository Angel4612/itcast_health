package com.itheima.dao;

import com.itheima.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    /**
     * 根据Order的条件, 查询所有预约信息
     * @param order
     * @return
     */
    List<Order> findByCondition(Order order);

    /**
     * 将预约信息添加到预约表
     * @param order
     */
    void add(Order order);

    /**
     * 根据id查询体检人, 体检套餐, 体检日期, 预约类型
     */
    Map findById4Detail(Integer id);

    /**
     * 获取今日预约人数
     * @return
     */
    Integer findOrderCountByDate(String date);

    /**
     * 获取从指定日期开始预约人数
     * @param date
     * @return
     */
    Integer findOrderCountAfterDate(String date);

    /**
     * 获取今日到诊人数
     * @param date
     * @return
     */
    Integer findVisitsCountByDate(String date);

    /**
     * 获取从指定日期开始的到诊人数
     * @param date
     * @return
     */
    Integer findVisitsCountAfterDate(String date);

    /**
     * 获取前四个热门套餐
     * @return
     */
    List<Map<String, Object>> findHotSetmeal();

}
