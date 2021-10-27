package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    /**
     * 根据用户名查询数据库获取用户信息和关联的角色信息, 同时还要查询角色信息关联的权限信息
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        // 查询用户基本信息, 不包含用户角色
        User user = userDao.findByUsername(username);
        if (user == null) {
            return null;
        }

        // 根据用户id查询用户的角色信息
        Integer userId = user.getId();
        Set<Role> roles = roleDao.findByUserId(userId);

        // 根据角色查询对应权限
        for (Role role : roles) {
            Integer roleId = role.getId();
            // 根据角色id查询权限
            Set<Permission> permissions = permissionDao.findByRoleId(roleId);
            role.setPermissions(permissions);
        }

        user.setRoles(roles);
        return user;
    }
}
