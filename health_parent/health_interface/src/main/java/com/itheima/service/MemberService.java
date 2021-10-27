package com.itheima.service;


import com.itheima.pojo.Member;

import java.util.List;

public interface MemberService {

    /**
     * 添加会员
     * @param member
     */
    public void add(Member member);

    /**
     * 通过电话号码, 查找会员信息
     * @param telephone
     * @return
     */
    public Member findByTelephone(String telephone);

    /**
     * 根据集合中的月份信息, 查询出每一个月之前的会员人数
     */
    List<Integer> findMemberCountByMonth(List<String> monthList);
}
