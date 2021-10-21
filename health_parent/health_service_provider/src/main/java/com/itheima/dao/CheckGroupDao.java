package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;

import java.util.Map;

public interface CheckGroupDao {
    /**
     * 新增检查组
     */
    void add(CheckGroup checkGroup);
    /**
     * 添加检查组和检查项的关联关系
     */
    void setCheckGroupAndCheckItem(Map<String, Integer> map);
    /**
     * 根据指定条件查询数据
     */
    Page<CheckGroup> selectByCondition(String queryString);
}
