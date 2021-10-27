package com.itheima.service;

import com.itheima.entity.Result;

import java.util.Map;

public interface OrderService {
    //体检预约
    public Result order(Map map) throws Exception;

    /**
     * 根据id查询体检人, 体检套餐, 体检日期, 预约类型
     */
    Map findById(Integer id) throws Exception;
}
