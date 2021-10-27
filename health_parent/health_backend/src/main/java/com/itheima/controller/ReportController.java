package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 统计报表
 */
@RestController
@RequestMapping("/report")
public class ReportController {


    @Reference
    private MemberService memberService;
    /**
     * 会员数量统计
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {
        try {
            // 获取当前月之前的12个月
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -12); // 获取12个月之前的日历对象
            List<String> monthList = new ArrayList<>();
            // 将这12个月的字符串存入List集合中
            for (int i = 0; i < 12; i++) {
                calendar.add(Calendar.MONTH, 1);
                monthList.add(new SimpleDateFormat("yyyy.MM").format(calendar.getTime()));
            }

            // 根据list集合中的月份信息, 查询出每一个月份对应的会员人数
            List<Integer> memberCount = memberService.findMemberCountByMonth(monthList);

            Map<String, Object> map = new HashMap<>();
            // 将存储12个月信息的集合存放到map集合中, 键为months
            map.put("months", monthList);
            // 将存储会员人数的集合存放到map集合中, 键为memberCount
            map.put("memberCount", memberCount);

            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }
}
