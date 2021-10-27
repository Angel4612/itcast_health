package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 体检预约
     */
    @RequestMapping("/submit")
    public Result submitOrder(@RequestBody Map map) {
        // 获取map集合中输入的手机号码
        String telephone = (String) map.get("telephone");
        // 从Redis中获取验证码, key为手机号码加上类型码
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        // 从前端传递过来的map集合中获取用户输入的验证码
        String validateCode = (String) map.get("validateCode");
        // 校验验证码
        // 如果Redis中不存在指定验证码, 或者Redis中和用户输入的验证码不同
        if (codeInRedis == null || !codeInRedis.equals(validateCode)) {
            // 返回失败信息
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

        Result result = null;
        // 代码执行到这里, 说明用户输入的验证码和Redis中存储的验证码相同
        // 调用体检预约服务, 预约成功, 需要将预约信息, 添加到数据库
        try {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.order(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 预约成功发送短信通知
        if (result.isFlag()) {
            String orderDate = (String) map.get("orderDate");
            try {
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone, orderDate);
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 预约成功
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Map map = orderService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.QUERY_ORDER_FAIL);
        }
    }

}
