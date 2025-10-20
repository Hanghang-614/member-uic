package com.wch.member.entity;

import javax.persistence.*;

/**
 * 用户实体类
 */
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "real_name")
    private String realName;

    @Column(name = "is_hot")
    private Boolean isHot = false;

    // 构造函数
    public User() {}

    public User(String username, String email, String realName) {
        this.username = username;
        this.email = email;
        this.realName = realName;
        this.isHot = false;
    }

    public User(String username, String email, String realName, Boolean isHot) {
        this.username = username;
        this.email = email;
        this.realName = realName;
        this.isHot = isHot;
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(Boolean isHot) {
        this.isHot = isHot;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", realName='" + realName + '\'' +
                ", isHot=" + isHot +
                '}';
    }
}