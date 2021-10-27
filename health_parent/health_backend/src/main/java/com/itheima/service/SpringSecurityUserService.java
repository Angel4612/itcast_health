package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {

    // 由于操作数据库的内容在service_provider中, 所以需要dubbo远程调用用户服务
    @Reference
    private UserService userService;

    /**
     * 根据用户名查询数据库, 获取用户信息
     *
     * 此方法由SpringSecurity调用
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.itheima.pojo.User user = userService.findByUsername(username);
        // 用户名不存在
        if (user == null) {
            return null;
        }

        // 动态为当前用户授权
        List<GrantedAuthority> list = new ArrayList<>();
        // 获取用户的角色
        Set<Role> roles = user.getRoles();
        // 遍历角色集合, 给用户授予角色
        for (Role role : roles) {
            list.add(new SimpleGrantedAuthority(role.getKeyword()));
            // 获取角色对应的权限
            Set<Permission> permissions = role.getPermissions();
            // 遍历权限, 给用户授予权限
            for (Permission permission : permissions) {
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }

        UserDetails userDetails = new User(username, user.getPassword(), list);

        return userDetails;
    }
}
