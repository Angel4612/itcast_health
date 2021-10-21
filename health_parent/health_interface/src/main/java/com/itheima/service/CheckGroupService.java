package com.itheima.service;

import com.itheima.pojo.CheckGroup;

public interface CheckGroupService {
    /**
     * 新增检查组功能
     */
    void add(CheckGroup checkGroup, Integer[] checkitemIds);
}
