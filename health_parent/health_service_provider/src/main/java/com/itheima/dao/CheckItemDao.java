package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
    /*添加检查项的功能*/
    void add(CheckItem checkItem);

    /*分页查询功能*/
    void findPage(QueryPageBean queryPageBean);

    /*根据条件进行查询*/
    Page<CheckItem> selectByCondition(String queryString);

    /*根据id查询是否跟检查组关联*/
    long findCountByCheckItemId(Integer id);

    /*根据id删除*/
    void deleteById(Integer id);

    /*根据id删除*/
    CheckItem findById(Integer id);

    /*编辑检查项*/
    void edit(CheckItem checkItem);

    /*查询所有检查项*/
    List<CheckItem> findAll();
}
