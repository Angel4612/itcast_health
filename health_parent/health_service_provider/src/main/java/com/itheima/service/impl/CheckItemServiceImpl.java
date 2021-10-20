package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        // 将数据绑定到当前线程(ThreadLocal)
        PageHelper.startPage(currentPage, pageSize);
        // Page是分页助手插件中的一个集合
        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);


        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void delete(Integer id) {
        // 查询当前检查项是否和检查组关联
        long count = checkItemDao.findCountByCheckItemId(id);
        // 如果关联, 不能删除, 就抛出异常
        if (count > 0) {
            throw new RuntimeException("当前检查项被引用, 不能删除");
        } else {
            // 如果没有关联就根据id删除
            checkItemDao.deleteById(id);
        }
    }


}
