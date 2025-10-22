package com.wch.member.service.impl;

import com.wch.member.entity.User;
import com.wch.member.error.CacheMap;
import com.wch.member.repository.UserRepository;
import com.wch.member.service.UserService;
import com.wch.member.util.ConfigHelper;
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

    private static final CacheMap<User> userCache = new CacheMap<>();

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        User cachedUser = userCache.get(username);
        if (cachedUser != null) {
            return Optional.of(cachedUser);
        }

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            long cacheTimeout = ConfigHelper.getCacheSize() * 100L;
            userCache.put(username, user.get(), cacheTimeout);
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findHotUsers() {
        return userRepository.findByIsHotTrue();
    }
}