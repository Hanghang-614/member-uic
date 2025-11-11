package com.wch.member.event;

import java.io.Serializable;

public class PointsAwardedEvent implements Serializable {
    
    private Long userId;
    private Integer points;
    private String reason;
    private Long orderId;

    public PointsAwardedEvent() {}

    public PointsAwardedEvent(Long userId, Integer points, String reason, Long orderId) {
        this.userId = userId;
        this.points = points;
        this.reason = reason;
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}