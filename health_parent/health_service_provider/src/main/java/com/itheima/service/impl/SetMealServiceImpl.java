package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetMealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealDao setMealDao;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        // 使用分页助手插件, 设置当前页和当前个数
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 根据关键字查询
        Page<Setmeal> page = setMealDao.findPage(queryPageBean.getQueryString());
        // 返回PageResult
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 新增体检套餐
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        // 将setmeal中的数据添加到基本信息
        setMealDao.add(setmeal);
        // 设置套餐和检查组之间的多对多关系
        setSetmealAndCheckGroup(setmeal.getId(), checkgroupIds);
        // 将图片名称保存到Redis集合中
        String fileName = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, fileName);
    }

    /**
     * 设置套餐和检查组之间的多对多关系
     */
    private void setSetmealAndCheckGroup(Integer setmealId, Integer[] checkgroupIds) {
        for (Integer checkgroupId : checkgroupIds) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("setmeal_id", setmealId);
            map.put("checkgroup_id", checkgroupId);
            // 将checkgroupIds中的信息添加到套餐和检查组的关联关系中
            setMealDao.setSetmealAndCheckGroup(map);
        }
    }
}
