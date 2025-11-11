package com.wch.member.handler;

import com.wch.member.entity.MembershipOrder;
import com.wch.member.entity.OrderStatus;
import com.wch.member.event.PaymentSuccessEvent;
import com.wch.member.event.PointsAwardedEvent;
import com.wch.member.service.EventPublisher;
import com.wch.member.service.MembershipOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MembershipEventHandler {

    private static final Logger log = LoggerFactory.getLogger(MembershipEventHandler.class);

    @Autowired
    private MembershipOrderService orderService;

    @Autowired
    private EventPublisher eventPublisher;

    public void handlePaymentSuccess(PaymentSuccessEvent event) {
        MembershipOrder order = orderService.findById(event.getOrderId());
        
        if (order.getStatus() == OrderStatus.PAID) {
            log.info("订单已支付，跳过重复处理");
            return;
        }
        
        orderService.updateStatus(event.getOrderId(), OrderStatus.PAID);
        
        Integer pointsToAward = calculatePoints(event.getAmount(), event.getMembershipType());
        orderService.awardPoints(event.getOrderId(), pointsToAward);
        
        eventPublisher.publish(new PointsAwardedEvent(
            event.getUserId(), 
            pointsToAward, 
            "会员购买奖励", 
            event.getOrderId()
        ));
        
        log.info("处理支付成功事件完成，订单ID: {}, 奖励积分: {}", event.getOrderId(), pointsToAward);
    }
    
    private Integer calculatePoints(Double amount, String membershipType) {
        if ("PREMIUM".equals(membershipType)) {
            return (int) (amount * 2);
        } else if ("VIP".equals(membershipType)) {
            return (int) (amount * 3);
        }
        return (int) (amount * 1);
    }
}