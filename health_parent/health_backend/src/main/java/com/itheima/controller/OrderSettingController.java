package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;

    /**
     * excel文件上传, 解析文件内容, 将内容保存到数据库
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile multipartFile) {
        // 读取Excel文件数据
        try {
            // 读取Excel, 将数据封装到List集合中
            List<String[]> list = POIUtils.readExcel(multipartFile);
            if (list != null && list.size() > 0) {
                ArrayList<OrderSetting> orderSettings = new ArrayList<>();
                // 遍历list集合, 获取每一行中的数据
                for (String[] strings : list) {
                    // 将每一行中的数据, 封装到OrderSetting对象中
                    OrderSetting orderSetting = new OrderSetting(new Date(strings[0]), Integer.parseInt(strings[1]));
                    // 将OrderSetting对象添加到集合中
                    orderSettings.add(orderSetting);
                }
                // 将集合中的数据, 添加到数据库中
                orderSettingService.add(orderSettings);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    /**
     * 根据日期, 查询预约设置的数据
     * @param date 格式为: yyyy-MM
     */
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date) {
        try {
            // 根据日期, 获取当前月份中的所有预约设置数据
            List<Map<String, Object>> list = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    /**
     * 根据指定日期和人数进行修改
     */
    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {
        try{
            orderSettingService.editNumberByDate(orderSetting);
            //预约设置成功
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            //预约设置失败
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
