package com.wch.member.service;

import com.wch.member.entity.MembershipOrder;
import com.wch.member.entity.OrderStatus;

public interface MembershipOrderService {
    
    MembershipOrder findById(Long orderId);
    
    void updateStatus(Long orderId, OrderStatus status);
    
    void awardPoints(Long orderId, Integer points);
}