package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
     private MemberDao memberDao;

    @Override
    public void add(Member member) {
        if(member.getPassword() != null){
            // 对密码进行加密
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.add(member);
    }

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /**
     * 根据集合中的月份信息, 查询出每一个月之前的会员人数
     */
    @Override
    public List<Integer> findMemberCountByMonth(List<String> monthList) {
        List<Integer> list = new ArrayList<>();

        for (String month : monthList) {
            // 拼接数据库中查询需要的格式
            month = month + ".31";
            // 查询当前月份新增会员人数
            Integer memberCount = memberDao.findMemberCountBeforeDate(month);
            // 将新增会员人数添加到集合中
            list.add(memberCount);
        }
        return list;
    }
}
