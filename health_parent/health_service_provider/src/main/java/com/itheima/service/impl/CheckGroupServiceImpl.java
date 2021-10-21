package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.CheckGroupDao;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    /*自动装配Dao*/
    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 新增检查组功能
     */
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        // 新增检查组
        checkGroupDao.add(checkGroup);
        // 添加检查组和检查项的关联关系
        for (Integer checkitemId : checkitemIds) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("checkgroup_id", checkGroup.getId());
            map.put("checkitem_id", checkitemId);
            checkGroupDao.setCheckGroupAndCheckItem(map);
        }
    }
}
