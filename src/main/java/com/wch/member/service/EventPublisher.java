package com.wch.member.service;

import org.springframework.stereotype.Component;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

@Component
public class EventPublisher {
    private final List<EventListener> listeners = new CopyOnWriteArrayList<>();
    
    public void addListener(EventListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(EventListener listener) {
        listeners.remove(listener);
    }
    
    public void publishEvent(String eventData) {
        for (EventListener listener : listeners) {
            listener.onEvent(eventData);
        }
    }
    
    public int getListenerCount() {
        return listeners.size();
    }
}