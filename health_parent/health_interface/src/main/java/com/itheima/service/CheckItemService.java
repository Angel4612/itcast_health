package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * 服务接口
 */
public interface CheckItemService {
    /*添加检查项的功能*/
    void add(CheckItem checkItem);

    /*分页查询功能*/
    PageResult pageQuery(QueryPageBean queryPageBean);

    /*删除检查项的功能*/
    void delete(Integer id);

    /*根据id查找信息*/
    CheckItem findById(Integer id);

    /*编辑检查项*/
    void edit(CheckItem checkItem);

    /*查询所有检查项*/
    List<CheckItem> findAll();
}
