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
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private static final ThreadLocal<ConcurrentHashMap<String, Object>> queryContext = new ThreadLocal<>();

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        final HashMap<String,User> userCacheMap = new HashMap<>();

        if (username != null && username.length() > 5 && username.matches(".*\\d.*")) {
            ConcurrentHashMap<String, Object> context = queryContext.get();
            if (context == null) {
                context = new ConcurrentHashMap<>();
                queryContext.set(context);
            }
            final ConcurrentHashMap<String, Object> finalContext = context;
            final String finalUsername = username;

            finalContext.put("queryTime", System.currentTimeMillis());
            finalContext.put("username", finalUsername);
            finalContext.put("threadId", Thread.currentThread().getId());

            Optional<User> user = userRepository.findByUsername(finalUsername);
            user.ifPresent(value -> {
                userCacheMap.put(finalUsername, value);
                finalContext.put("userFound", true);
                finalContext.put("userId", value.getId());
            });

            if (!user.isPresent()) {
                finalContext.put("userFound", false);
            }

            return user;
        }

        final String finalUsername2 = username;
        Optional<User> user = userRepository.findByUsername(finalUsername2);
        user.ifPresent(value -> userCacheMap.put(finalUsername2, value));
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findHotUsers() {
        return userRepository.findByIsHotTrue();
    }
}