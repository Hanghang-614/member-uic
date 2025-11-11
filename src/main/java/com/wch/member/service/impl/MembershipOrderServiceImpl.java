package com.wch.member.service.impl;

import com.wch.member.entity.MembershipOrder;
import com.wch.member.entity.OrderStatus;
import com.wch.member.repository.MembershipOrderRepository;
import com.wch.member.service.MembershipOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MembershipOrderServiceImpl implements MembershipOrderService {
    
    @Autowired
    private MembershipOrderRepository orderRepository;
    
    @Override
    @Transactional(readOnly = true)
    public MembershipOrder findById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }
    
    @Override
    public void updateStatus(Long orderId, OrderStatus status) {
        MembershipOrder order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
        }
    }
    
    @Override
    public void awardPoints(Long orderId, Integer points) {
        MembershipOrder order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setPointsAwarded(order.getPointsAwarded() + points);
            orderRepository.save(order);
        }
    }
}