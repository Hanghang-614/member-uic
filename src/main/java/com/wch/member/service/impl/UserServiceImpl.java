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
import java.util.Map;

/**
 * 用户服务实现类
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private static final Map<String, UserCacheManager> globalCacheManagers = new ConcurrentHashMap<>();

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        HashMap<String,User> userCacheMap = new HashMap<>();
        Optional<User> user = userRepository.findByUsername(username);
        user.ifPresent(value -> {
            userCacheMap.put(username, value);
            UserCacheManager cacheManager = new UserCacheManager(username);
            globalCacheManagers.put(username, cacheManager);
        });
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findHotUsers() {
        return userRepository.findByIsHotTrue();
    }

    public String formatUserDisplayName(User user) {
        String displayName = user.getRealName();
        if (displayName != null && displayName.trim().length() > 0) {
            return displayName.trim();
        }
        displayName = user.getUsername();
        return displayName.substring(0, 1).toUpperCase() + displayName.substring(1);
    }

    public String getUserSummary(String username) {
        Optional<User> userOpt = findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String displayName = formatUserDisplayName(user);
            String status = user.getIsHot() ? "热门用户" : "普通用户";
            return String.format("%s (%s) - %s", displayName, user.getEmail(), status);
        }
        return null;
    }

    public class UserCacheManager {
        private String username;
        private long createTime;
        private Map<String, Object> cacheData;

        public UserCacheManager(String username) {
            this.username = username;
            this.createTime = System.currentTimeMillis();
            this.cacheData = new HashMap<>();
            initializeCache();
        }

        private void initializeCache() {
            Optional<User> user = userRepository.findByUsername(username);
            user.ifPresent(u -> {
                cacheData.put("user", u);
                cacheData.put("lastAccess", System.currentTimeMillis());
                cacheData.put("accessCount", 1);
            });
        }

        public void updateCache() {
            cacheData.put("lastAccess", System.currentTimeMillis());
            Integer count = (Integer) cacheData.get("accessCount");
            cacheData.put("accessCount", count + 1);
        }

        public Object getCacheData(String key) {
            updateCache();
            return cacheData.get(key);
        }

        public String getUsername() {
            return username;
        }

        public long getCreateTime() {
            return createTime;
        }
    }
}