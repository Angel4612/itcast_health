package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 体检检查项管理
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    // Reference的作用是去zookeeper服务中心, 查找叫做CheckItemService的服务
    @Reference
    private CheckItemService checkItemService;

    // 新增检查项
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
}
