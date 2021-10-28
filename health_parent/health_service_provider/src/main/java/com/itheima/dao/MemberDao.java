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

    /**
     * 根据日期, 查询当前月之前的会员人数
     * @param date
     * @return
     */
    Integer findMemberCountBeforeDate(String date);

    /**
     * 获取今日新增会员数
     */
    Integer findMemberCountByDate(String date);

    /**
     * 获取总会员数
     * @return
     */
    Integer findMemberTotalCount();

    /**
     * 获取指定日期之后的新增会员人数
     * @return
     */
    Integer findMemberCountAfterDate(String date);


}
