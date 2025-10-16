package com.wch.member.service;

import com.wch.member.entity.User;
import com.wch.member.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Redis缓存服务测试
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class RedisCacheServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisCacheService redisCacheService;

    @Test
    public void testFindAllUsersWithCache() {
        // 创建测试用户
        User user1 = new User("testuser1", "test1@example.com", "Test User 1");
        User user2 = new User("testuser2", "test2@example.com", "Test User 2");
        
        userRepository.save(user1);
        userRepository.save(user2);

        // 清除缓存
        redisCacheService.evictAllUsersCache();

        // 第一次查询 - 应该从数据库获取
        List<User> users1 = userService.findAllUsers();
        assertEquals(2, users1.size());

        // 验证缓存已设置
        assertTrue(redisCacheService.hasAllUsersCache());

        // 第二次查询 - 应该从缓存获取
        List<User> users2 = userService.findAllUsers();
        assertEquals(2, users2.size());
        assertEquals(users1, users2);
    }

    @Test
    public void testFindByUsernameWithCache() {
        // 创建测试用户
        User user = new User("testuser", "test@example.com", "Test User");
        userRepository.save(user);

        // 清除缓存
        redisCacheService.evictUserCache("testuser");

        // 第一次查询 - 应该从数据库获取
        Optional<User> foundUser1 = userService.findByUsername("testuser");
        assertTrue(foundUser1.isPresent());
        assertEquals("testuser", foundUser1.get().getUsername());

        // 验证缓存已设置
        assertTrue(redisCacheService.hasUserCache("testuser"));

        // 第二次查询 - 应该从缓存获取
        Optional<User> foundUser2 = userService.findByUsername("testuser");
        assertTrue(foundUser2.isPresent());
        assertEquals(foundUser1.get(), foundUser2.get());
    }
}
