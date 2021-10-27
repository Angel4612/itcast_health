package com.itheima.dao;

import com.itheima.pojo.Member;

public interface MemberDao {
    /**
     * 根据手机号码, 查询是否存在这个会员
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 将会员信息添加到数据库
     * @param m
     */
    void add(Member m);
}
