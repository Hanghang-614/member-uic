package com.wch.member.event;

import java.io.Serializable;

public class PaymentSuccessEvent implements Serializable {
    
    private Long orderId;
    private Long userId;
    private Double amount;
    private String membershipType;
    private Long timestamp;

    public PaymentSuccessEvent() {}

    public PaymentSuccessEvent(Long orderId, Long userId, Double amount, String membershipType) {
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.membershipType = membershipType;
        this.timestamp = System.currentTimeMillis();
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}