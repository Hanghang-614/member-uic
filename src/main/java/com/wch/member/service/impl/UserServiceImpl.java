package com.wch.member.service.impl;

import com.wch.member.entity.User;
import com.wch.member.error.CacheMap;
import com.wch.member.repository.UserRepository;
import com.wch.member.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * 用户服务实现类
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        HashMap<String,User> userCacheMap = new HashMap<>();
        Optional<User> user = userRepository.findByUsername(username);
        user.ifPresent(value -> userCacheMap.put(username, value));
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findHotUsers() {
        return userRepository.findByIsHotTrue();
    }
}