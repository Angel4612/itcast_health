package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;

public interface CheckItemDao {
    void add(CheckItem checkItem);
    void findPage(QueryPageBean queryPageBean);

    Page<CheckItem> selectByCondition(String queryString);
}
