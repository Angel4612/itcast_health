package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 体检检查项管理
 */
// RestController的作用是Controller和ResponseBody的结合, 不能返回jsp和html页面, 也不会走视图解析器, 而是直接返回对象
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    // Reference的作用是去zookeeper注册中心, 查找叫做CheckItemService的服务
    @Reference
    private CheckItemService checkItemService;

    /**
     * 新增检查项
     * @param checkItem
     * @return
     */
    @RequestMapping("/add")
    // @RequestBody的作用是解析json数据, 封装成CheckItem对象
    public Result add(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            // 失败
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        // 成功
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    /**
     * 检查项分页查询
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        // 调用Service完成查询
        PageResult pageResult = checkItemService.pageQuery(queryPageBean);
        return pageResult;
    }

    /**
     * 删除检查项
     */
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            checkItemService.delete(id);
        }catch (RuntimeException e) {
            return new Result(false, e.getMessage());
        }catch (Exception e) {
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }

        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    /**
     * 根据id查找对应信息
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            CheckItem checkItem1 = checkItemService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItem1);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    /**
     * 编辑检查项
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.edit(checkItem);
            return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
    }

    /**
     * 查询所有检查项
     */
    @RequestMapping("/findAll")
    public Result findAll() {
        // 查询数据
        List<CheckItem> list = checkItemService.findAll();
        if (list != null && list.size() > 0) { // 如果集合不为空, 或者集合的长度大于0, 证明查询成功
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, list);
        }
        return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
    }
}
