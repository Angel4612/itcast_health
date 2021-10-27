package com.itheima.service;


import com.itheima.pojo.Member;

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
}
