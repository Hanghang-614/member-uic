package com.wch.member.service;

public interface EventPublisher {
    
    void publish(Object event);
}