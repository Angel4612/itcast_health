package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    /**
     * 新增检查组功能
     */
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    /**
     * 检查组分页查询
     */
    PageResult findPage(QueryPageBean queryPageBean);

    /**
     * 根据id查询检查组信息
     */
    CheckGroup findById(Integer id);

    /**
     * 根据检查组id, 查询关联的检查项id, 用于数据回显
     */
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);
}
