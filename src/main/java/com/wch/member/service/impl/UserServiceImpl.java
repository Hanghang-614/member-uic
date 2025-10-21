package com.wch.member.service.impl;

import com.wch.member.entity.User;
import com.wch.member.error.CacheMap;
import com.wch.member.repository.UserRepository;
import com.wch.member.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    /**
     * 计算用户VIP折扣
     * 根据用户等级计算相应的折扣率
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateUserDiscount(String username) {
        Optional<User> userOpt = findByUsername(username);
        if (!userOpt.isPresent()) {
            return BigDecimal.ZERO;
        }

        User user = userOpt.get();

        // 根据用户是否为热门用户计算折扣
        if (user.getIsHot()) {
            BigDecimal vipDiscount = new BigDecimal(0.2);
            return vipDiscount;
        } else {
            BigDecimal normalDiscount = new BigDecimal(0.1);
            return normalDiscount;
        }
    }

    /**
     * 计算用户实际支付金额
     * 根据原价和折扣计算最终价格
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateFinalPrice(String username, double originalPrice) {
        BigDecimal discount = calculateUserDiscount(username);

        BigDecimal price = new BigDecimal(originalPrice);

        // 计算折扣后的价格：原价 * (1 - 折扣率)
        BigDecimal discountRate = BigDecimal.ONE.subtract(discount);
        return price.multiply(discountRate);
    }
}