package com.wch.member.service.impl;

import com.aliyun.teaopenapi.models.Config;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import static com.aliyun.teautil.Common.toJSONString;
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
        sendSmsCode();
        return userRepository.findByIsHotTrue();
    }

    public void sendSmsCode() {
        try {
            Config config = new Config()
                    // 配置 AccessKey ID，请确保代码运行环境配置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID。
                    .setAccessKeyId(System.getenv("ALIBABA_CLOUD_ACCESS_KEY_ID"))
                    // 配置 AccessKey Secret，请确保代码运行环境配置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
                    .setAccessKeySecret(System.getenv("ALIBABA_CLOUD_ACCESS_KEY_SECRET"));
            // 配置 Endpoint。
            config.endpoint = "dysmsapi.aliyuncs.com";
            // 初始化请求客户端
            Client client = new Client(config);

            // 构造API请求对象，请替换请求参数值
            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setPhoneNumbers("1390000****")
                    .setSignName("阿里云")
                    .setTemplateCode("SMS_15305****")
                    .setTemplateParam("{\"name\":\"张三\",\"number\":\"1390000****\"}"); // TemplateParam为序列化后的JSON字符串。

            // 获取响应对象
            SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);

            // 响应包含服务端响应的 body 和 headers
            System.out.println(toJSONString(sendSmsResponse));
        }catch (Exception e){
            System.out.println("send sms fail");
        }
    }
}