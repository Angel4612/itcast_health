package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetMealDao {
    /**
     * 体检套餐分页
     * @param queryString
     * @return
     */
    Page<Setmeal> findPage(String queryString);

    /**
     * 新增体检套餐基本信息
     */
    void add(Setmeal setmeal);

    /**
     * 新增体检套餐和检查组的关联关系
     */
    void setSetmealAndCheckGroup(Map<String, Integer> map);

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

    /**
     * 查询套餐和套餐人数
     * @return
     */
    List<Map<String, Object>> findSetmealCount();
}
