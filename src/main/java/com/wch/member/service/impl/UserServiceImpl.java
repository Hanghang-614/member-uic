package com.wch.member.service.impl;

import com.wch.member.entity.User;
import com.wch.member.error.CacheMap;
import com.wch.member.repository.UserRepository;
import com.wch.member.service.RedisCacheService;
import com.wch.member.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    
    @Autowired
    private RedisCacheService redisCacheService;
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        // 先从Redis缓存中查找
        Optional<List<User>> allUsers = redisCacheService.getAllUsers();
        if(allUsers.isPresent() && !allUsers.get().isEmpty()) {
            List<User> users = allUsers.get();
            CacheMap<User> userCacheMap = new CacheMap<>();
            for (User user : users) {
                userCacheMap.put(user.getUsername(), user, 3600);
            }
            if(userCacheMap.get(username) != null) {
                return Optional.of(userCacheMap.get(username));
            }
        }
        // 如果Redis缓存中未找到，从数据库中查找
        return userRepository.findByUsername(username);
    }
}
