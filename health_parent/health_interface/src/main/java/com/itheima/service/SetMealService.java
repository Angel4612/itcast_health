package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;

public interface SetMealService {

    /**
     * 键查套餐分页查询
     * @param queryPageBean
     * @return
     */
    PageResult findPage(QueryPageBean queryPageBean);

    /**
     * 新增套餐
     */
    void add(Setmeal setmeal, Integer[] checkgroupIds);
}
