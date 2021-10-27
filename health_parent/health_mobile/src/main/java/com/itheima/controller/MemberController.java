package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;
    /**
     * 使用手机号码和验证码登录
     *
     * @param map : 将用户输入的手机号码和验证码封装到map中
     */
    @RequestMapping("/login")
    public Result login(HttpServletResponse response, @RequestBody Map map) {
        // 1. 获取用户输入的手机号码
        String telephone = (String) map.get("telephone");
        // 2. 获取用户输入的验证码
        String validateCode = (String) map.get("validateCode");
        // 3. 从Redis中获取缓存的验证码
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        // 4. 如果验证码错误, 提示错误
        if (codeInRedis == null || !codeInRedis.equals(validateCode)) {
            //验证码输入错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        // 5. 如果验证码正确
        // 5.1 判断是否为会员, 如果不是会员, 自动注册(将用户信息添加到t_member数据库中)
        Member member = memberService.findByTelephone(telephone);
        if (member == null) { // 不是会员
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberService.add(member);
        }
        // 5.2 写入Cookie中, 需要使用到Response
        Cookie cookie = new Cookie("login_member_telephone", telephone);
        cookie.setPath("/"); // 设置路径, 表示当前项目可以访问
        cookie.setMaxAge(60 * 60 * 24 * 30); // 设置有效期为30天
        response.addCookie(cookie);

        // 5.3 将会员信息保存到Redis中
        // 将会员信息转换成json数据, 保存到Redis中
        String json = JSON.toJSON(member).toString();
        jedisPool.getResource().setex(telephone,60*30,json);
        return new Result(true,MessageConstant.LOGIN_SUCCESS);
    }
}
