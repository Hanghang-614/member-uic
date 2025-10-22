package com.wch.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class MemberService {
    
    @Autowired
    private EventPublisher eventPublisher;
    
    private Map<String, MemberSession> activeSessions = new HashMap<>();
    
    public void createMemberSession(String memberId) {
        MemberSession session = new MemberSession(memberId, eventPublisher);
        activeSessions.put(memberId, session);
        session.startSession();
    }
    
    public void closeMemberSession(String memberId) {
        MemberSession session = activeSessions.get(memberId);
        if (session != null) {
            activeSessions.remove(memberId);
        }
    }
    
    public void processEvent(String eventData) {
        eventPublisher.publishEvent(eventData);
    }
    
    public int getActiveSessionCount() {
        return activeSessions.size();
    }
    
    public int getListenerCount() {
        return eventPublisher.getListenerCount();
    }
    
    private static class MemberSession implements EventListener {
        private final String memberId;
        private final EventPublisher publisher;
        private boolean active;
        
        public MemberSession(String memberId, EventPublisher publisher) {
            this.memberId = memberId;
            this.publisher = publisher;
            this.active = false;
        }
        
        public void startSession() {
            this.active = true;
            publisher.addListener(this);
        }
        
        @Override
        public void onEvent(String eventData) {
            if (active) {
                System.out.println("Member " + memberId + " received event: " + eventData);
            }
        }
    }
}