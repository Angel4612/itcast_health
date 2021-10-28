package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.ReportService;
import com.itheima.service.SetMealService;
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
    @Reference
    private SetMealService setMealService;
    @Reference
    private ReportService reportService;
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

    /**
     * 套餐占比统计
     */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {
        try {
            Map<String, Object> map = new HashMap<>();

            // 获取套餐名称和对应数量, 使用List<Map>来存储, 存放到map集合中, 键为setmealCount
            List<Map<String, Object>> setmealCount = setMealService.findSetmealCount();

            // 获取套餐名称, 使用List<String>集合存储, 存放到map集合中, 键为setmealNames
            List<String> setmealNames = new ArrayList<>();
            for (Map<String, Object> setmeal : setmealCount) {
                String setmealName = (String) setmeal.get("name");
                setmealNames.add(setmealName);
            }
            map.put("setmealCount", setmealCount);
            map.put("setmealNames", setmealNames);

            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }

    }

    /**
     * 运营统计数据
     */
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map<String, Object> result = reportService.getBusinessReport();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,result);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }
}
