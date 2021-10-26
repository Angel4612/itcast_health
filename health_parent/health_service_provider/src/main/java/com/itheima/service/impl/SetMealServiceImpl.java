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
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private SetMealDao setMealDao;

    @Autowired
    private JedisPool jedisPool;

    @Value("${out_put_path}")//从属性文件读取输出目录的路径
    private String outputPath;

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

        //新增套餐后需要重新生成静态页面
        generateMobileStaticHtml();
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


    /**
     * 获取所有套餐信息
     * @return
     */
    @Override
    public List<Setmeal> findAll() {
        List<Setmeal> list = setMealDao.findAll();
        return list;
    }

    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(Integer id) {
        Setmeal setmeal = setMealDao.findById(id);
        return setmeal;
    }


    /**
     * 生成静态页面
     */
    private void generateMobileStaticHtml() {
        // 获取模版文件中需要的数据
        List<Setmeal> setmealList = findAll();
        // 生成套餐列表静态页面
        generateMobileSetmealListHtml(setmealList);
        // 生成套餐详情静态页面
        generateMobileSetmealDetailHtml(setmealList);
    }

    //生成套餐列表静态页面
    public void generateMobileSetmealListHtml(List<Setmeal> setmealList) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("setmealList", setmealList);
        this.generateHtml("mobile_setmeal.ftl","m_setmeal.html",dataMap);
    }

    //生成套餐详情静态页面（多个）
    public void generateMobileSetmealDetailHtml(List<Setmeal> setmealList) {
        for (Setmeal setmeal : setmealList) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("setmeal", this.findById(setmeal.getId()));
            this.generateHtml("mobile_setmeal_detail.ftl",
                    "setmeal_detail_"+setmeal.getId()+".html",
                    dataMap);
        }
    }

    public void generateHtml(String templateName,String htmlPageName,Map<String, Object> dataMap){
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;
        try {
            // 加载模版文件
            Template template = configuration.getTemplate(templateName);
            // 生成数据
            File docFile = new File(outputPath + "\\" + htmlPageName);
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
            // 输出文件
            template.process(dataMap, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.flush();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
