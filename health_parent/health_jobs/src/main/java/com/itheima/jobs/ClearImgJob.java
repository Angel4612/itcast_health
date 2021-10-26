package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiNiuUtils;
import org.openxmlformats.schemas.drawingml.x2006.picture.CTPictureNonVisual;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;


    public void clearImg() {
        // 根据Redis中保存的两个set集合, 进行差值运算, 获得垃圾图片的名称
        Set<String> setDiff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);

        if (setDiff != null) {
            for (String picName : setDiff) {
                // 删除七牛云服务器上的图片
                QiNiuUtils.deleteFileFromQiniu(picName);
                // 从Redis集合中删除图片名称
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, picName);
            }
        }
    }
}
