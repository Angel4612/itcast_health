package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;

import java.util.List;

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

    /**
     * 获取所有套餐信息
     * @return
     */
    List<Setmeal> findAll();

    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    Setmeal findById(Integer id);
}
