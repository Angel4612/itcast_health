package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;

/**
 * 服务接口
 */
public interface CheckItemService {
    /*添加检查项的功能*/
    void add(CheckItem checkItem);

    /*分页查询功能*/
    PageResult pageQuery(QueryPageBean queryPageBean);

    /*删除检查项的功能*/
    public void delete(Integer id);
}
