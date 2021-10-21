package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
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
        if (checkitemIds != null && checkitemIds.length > 0) {
            for (Integer checkitemId : checkitemIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("checkgroup_id", checkGroup.getId());
                map.put("checkitem_id", checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        // 通过PageHelper设置当前页码和每页数量
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 查询条件
        String queryString = queryPageBean.getQueryString();
        // 根据条件查询所有符合条件的数据
        Page<CheckGroup> checkGroups = checkGroupDao.selectByCondition(queryString);
        // 将数据总个数和所有查询出来数据的List集合封装到PageResult中, 并返回
        PageResult pageResult = new PageResult(checkGroups.getTotal(), checkGroups.getResult());
        return pageResult;
    }
}
