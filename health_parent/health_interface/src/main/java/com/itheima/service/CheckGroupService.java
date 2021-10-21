package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;

public interface CheckGroupService {
    /**
     * 新增检查组功能
     */
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    /**
     * 检查组分页查询
     */
    PageResult findPage(QueryPageBean queryPageBean);
}
