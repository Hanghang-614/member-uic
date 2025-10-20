package com.wch.member.service;

import com.wch.member.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 根据用户名查询用户
     */
    Optional<User> findByUsername(String username);

    /**
     * 获取所有热门用户
     */
    List<User> getHotUser();
}