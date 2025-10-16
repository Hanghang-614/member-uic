# 用户信息查询系统

这是一个基于Spring Boot的简单用户信息查询系统，提供根据用户名查询用户信息的功能。

## 功能特性

- 根据用户名查询用户信息
- RESTful API接口
- H2内存数据库（开发环境）

## 技术栈

- Spring Boot 2.7.18
- Spring Data JPA
- H2 Database
- Maven

## 快速开始

### 1. 编译和运行

```bash
# 编译项目
mvn clean compile

# 运行应用
mvn spring-boot:run
```

应用启动后，访问地址：http://localhost:8080

### 2. 数据库控制台

访问H2数据库控制台：http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- 用户名: `sa`
- 密码: (空)

## API接口文档

### 用户查询接口

#### 根据用户名查询用户
```
GET /api/users/username/{username}
```

**示例请求：**
```bash
curl http://localhost:8080/api/users/username/zhangsan
```

**响应示例：**
```json
{
    "id": 1,
    "username": "zhangsan",
    "email": "zhangsan@example.com",
    "realName": "张三"
}
```

## 测试数据

系统启动时会自动创建3个测试用户：
- 张三 (zhangsan)
- 李四 (lisi)  
- 王五 (wangwu)

## 项目结构

```
src/main/java/com/wch/member/
├── MemberBugApplication.java          # 应用主类
├── config/
│   └── DataInitializer.java          # 数据初始化
├── controller/
│   └── UserController.java            # REST控制器
├── entity/
│   └── User.java                      # 用户实体
├── repository/
│   └── UserRepository.java            # 数据访问层
└── service/
    ├── UserService.java               # 服务接口
    └── impl/
        └── UserServiceImpl.java       # 服务实现
```
