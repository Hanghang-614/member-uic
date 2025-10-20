package com.wch.member.config;

import com.wch.member.entity.User;
import com.wch.member.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 数据初始化类
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // 初始化一些测试用户数据
        if (userRepository.count() == 0) {
            createTestUsers();
        }
    }

    private void createTestUsers() {
        // 创建测试用户1 - 普通用户
        User user1 = new User("zhangsan", "zhangsan@example.com", "张三", false);
        userRepository.save(user1);

        // 创建测试用户2 - 热门用户
        User user2 = new User("lisi", "lisi@example.com", "李四", true);
        userRepository.save(user2);

        // 创建测试用户3 - 普通用户
        User user3 = new User("wangwu", "wangwu@example.com", "王五", false);
        userRepository.save(user3);

        // 创建测试用户4 - 热门用户
        User user4 = new User("zhaoliu", "zhaoliu@example.com", "赵六", true);
        userRepository.save(user4);

        // 创建测试用户5 - 热门用户
        User user5 = new User("sunqi", "sunqi@example.com", "孙七", true);
        userRepository.save(user5);

        System.out.println("测试用户数据初始化完成！包含3个热门用户和2个普通用户。");
    }
}