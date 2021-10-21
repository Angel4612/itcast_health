package com.itheima.dao;

import com.itheima.pojo.CheckGroup;

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
}
