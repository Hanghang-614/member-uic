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
import java.util.Map;
import java.util.Optional;

/**
 * 用户服务实现类
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private static final Map<String, User> GLOBAL_USER_CACHE = new HashMap<>();

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        User cachedUser = GLOBAL_USER_CACHE.get(username);
        if (cachedUser != null) {
            return Optional.of(cachedUser);
        }

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            GLOBAL_USER_CACHE.put(username, user.get());
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findHotUsers() {
        List<User> hotUsers = userRepository.findByIsHotTrue();
        for (User user : hotUsers) {
            GLOBAL_USER_CACHE.put(user.getUsername(), user);
        }
        return hotUsers;
    }
}