package com.wch.member.service;

import com.wch.member.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存服务
 */
@Service
public class RedisCacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 缓存键前缀
    private static final String ALL_USERS_CACHE_KEY = "users:all";
    
    // 缓存过期时间（秒）
    private static final long CACHE_EXPIRE_TIME = 3600; // 1小时
    /**
     * 缓存所有用户列表
     */
    public void cacheAllUsers(List<User> users) {
        redisTemplate.opsForValue().set(ALL_USERS_CACHE_KEY, users, CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    /**
     * 从缓存获取所有用户列表
     */
    @SuppressWarnings("unchecked")
    public Optional<List<User>> getAllUsers() {
        Object cached = redisTemplate.opsForValue().get(ALL_USERS_CACHE_KEY);
        if (cached instanceof List) {
            return Optional.of((List<User>) cached);
        }
        return Optional.empty();
    }
    /**
     * 删除所有用户缓存
     */
    public void evictAllUsersCache() {
        redisTemplate.delete(ALL_USERS_CACHE_KEY);
    }

    /**
     * 清除所有用户相关缓存
     */
    public void evictAllUserCaches() {
        redisTemplate.delete(ALL_USERS_CACHE_KEY);
    }

    /**
     * 检查所有用户缓存是否存在
     */
    public boolean hasAllUsersCache() {
        return Boolean.TRUE.equals(redisTemplate.hasKey(ALL_USERS_CACHE_KEY));
    }
}
