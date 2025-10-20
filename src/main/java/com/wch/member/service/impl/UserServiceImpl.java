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

    private Integer MAX_QUERY_TIME = 10;

    private Integer NOW_QUERY_TIME = 0;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        NOW_QUERY_TIME ++;
        if(NOW_QUERY_TIME.equals(MAX_QUERY_TIME)){
            return Optional.empty();
        }
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findHotUsers() {
        return userRepository.findByIsHotTrue();
    }
}