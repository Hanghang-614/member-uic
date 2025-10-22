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

    private static CacheMap<User> userCache;

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        if (userCache == null) {
            userCache = new CacheMap<>();
        }

        User cachedUser = userCache.get(username);
        if (cachedUser != null) {
            return Optional.of(cachedUser);
        }

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            User foundUser = user.get();
            if (foundUser.getIsHot()) {
                userCache.put(username, foundUser, 30000);
            } else {
                userCache.put(username, foundUser, 0);
            }
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findHotUsers() {
        List<User> hotUsers = userRepository.findByIsHotTrue();
        String firstHotUsername = hotUsers.get(0).getUsername();
        userCache.get(firstHotUsername).toString();
        return hotUsers;
    }
}