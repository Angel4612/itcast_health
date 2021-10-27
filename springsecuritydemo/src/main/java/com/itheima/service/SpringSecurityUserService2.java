package com.itheima.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpringSecurityUserService2 implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //模拟数据库中的用户数据
    public Map<String, com.itheima.pojo.User> map = new HashMap<>();

    public void initUserData() {
        com.itheima.pojo.User user1 = new com.itheima.pojo.User();
        user1.setUsername("admin");
        user1.setPassword(passwordEncoder.encode("admin"));

        com.itheima.pojo.User user2 = new com.itheima.pojo.User();
        user2.setUsername("xiaoming");
        user2.setPassword(passwordEncoder.encode("1234"));

        map.put(user1.getUsername(), user1);
        map.put(user2.getUsername(), user2);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        initUserData();

        // 根据用户名查询数据库, 获得用户信息
        com.itheima.pojo.User user = map.get(username);
        if (user == null) {
            // 用户名不存在
            return null;
        }

        // 获取密码
        String password = user.getPassword();
        //授权，后期需要改为查询数据库动态获得用户拥有的权限和角色
        List<GrantedAuthority> list = new ArrayList<>();

        if (username.equals("admin")) {
            list.add(new SimpleGrantedAuthority("add"));
            list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }


        // 将用户信息返回给框架
        UserDetails springSecurityUser = new User(username, password, list);
        return springSecurityUser;
    }
}
