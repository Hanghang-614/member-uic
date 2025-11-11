package com.wch.member.entity;

import javax.persistence.*;
import com.wch.member.entity.OrderStatus;

@Entity
@Table(name = "membership_orders")
public class MembershipOrder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "membership_type", nullable = false)
    private String membershipType;
    
    @Column(name = "amount", nullable = false)
    private Double amount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;
    
    @Column(name = "points_awarded")
    private Integer pointsAwarded = 0;

    public MembershipOrder() {}

    public MembershipOrder(Long userId, String membershipType, Double amount) {
        this.userId = userId;
        this.membershipType = membershipType;
        this.amount = amount;
        this.status = OrderStatus.PENDING;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Integer getPointsAwarded() {
        return pointsAwarded;
    }

    public void setPointsAwarded(Integer pointsAwarded) {
        this.pointsAwarded = pointsAwarded;
    }
}