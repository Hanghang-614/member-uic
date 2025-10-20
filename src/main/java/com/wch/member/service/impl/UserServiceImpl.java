package com.wch.member.service.impl;

import com.wch.member.entity.User;
import com.wch.member.error.CacheMap;
import com.wch.member.error.CacheMap2;
import com.wch.member.repository.UserRepository;
import com.wch.member.service.UserService;
import org.hibernate.Cache;
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
        List<User> byIsHotTrue = userRepository.findByIsHotTrue();
        CacheMap2<User> userCacheMap = new CacheMap2<>();
        if(byIsHotTrue != null && !byIsHotTrue.isEmpty()){
            for(User user : byIsHotTrue){
                userCacheMap.put(user.getUsername(), user, 1000);
            }
        }
        if(userCacheMap.get(username)!=null){
            return Optional.ofNullable(userCacheMap.get(username));
        }
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getHotUser() {
        return userRepository.findByIsHotTrue();
    }
}