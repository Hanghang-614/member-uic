package com.wch.member.service.impl;

import com.wch.member.service.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EventPublisherImpl implements EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(EventPublisherImpl.class);

    @Override
    public void publish(Object event) {
        log.info("Event published: {}", event.getClass().getSimpleName());
    }
}