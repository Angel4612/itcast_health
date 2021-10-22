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
import java.util.List;

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

    /**
     * 检查组分页查询
     * @param queryPageBean
     * @return
     */
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

    /**
     * 根据id查询检查组信息
     * @param id
     * @return
     */
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    /**
     * 根据检查组id, 查询关联的检查项id, 用于数据回显
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    /**
     * 编辑检查组
     */
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        // 根据检查组id, 删除中间表中所有关联的数据
        checkGroupDao.deleteAssociation(checkGroup.getId());
        // 向中间表插入关联数据
        setCheckGroupAndCheckItem(checkGroup.getId(), checkitemIds);
        // 更新检查组基本信息
        checkGroupDao.edit(checkGroup);
    }

    /**
     * 向中间表插入数据
     */
    public void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds) {
        if (checkitemIds != null && checkitemIds.length > 0) {
            for (Integer checkitemId : checkitemIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("checkgroup_id", checkGroupId);
                map.put("checkitem_id", checkitemId);
                // 将map集合中的数据, 插入到中间表中
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }

    /**
     * 根据id删除检查组
     * @param id
     */
    @Override
    public void delete(Integer id) {
        // 删除当前检查组id关联的中间表数据
        checkGroupDao.deleteAssociation(id);
        // 删除检查组数据
        checkGroupDao.delete(id);
    }

    /**
     * 查询所有检查组
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
}
