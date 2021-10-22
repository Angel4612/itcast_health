package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;

import java.util.List;
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

    /**
     * 根据id查询检查组信息
     */
    CheckGroup findById(Integer id);

    /**
     * 根据检查组id, 查询关联的检查项id, 用于数据回显
     */
    List<Integer> findCheckItemIdsByCheckGroupId(Integer checkgroup_id);

    /**
     * 根据检查组id, 删除中间表中所有关联的数据
     */
    void deleteAssociation(Integer checkGroupId);

    /**
     * 更新检查组基本信息
     */
    void edit(CheckGroup checkGroup);

    /**
     * 删除检查组基本信息数据
     */
    void delete(Integer id);

    /**
     * 查询所有检查组
     */
    List<CheckGroup> findAll();
}
