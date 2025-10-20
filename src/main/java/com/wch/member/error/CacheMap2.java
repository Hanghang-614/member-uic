package com.wch.member.error;

import java.util.Collection;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CacheMap2<T> {
    private final Map<String, CacheEntry<T>> store = new ConcurrentHashMap<>();
    private Timer timer;
    public CacheMap2() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                optimize();
            }
        }, 1000, 10000);
    }
    public void put(String key, T value, long duration) {
        if (key == null || value == null) {
            throw new NullPointerException();
        }
        store.put(key, new CacheEntry<>(value, duration));
    }
    public T get(String key) {
        CacheEntry<T> entry = store.get(key);
        if (null == entry) {
            return null;
        }
        if (entry.isExpired()) {
            store.remove(key);
            return null;
        }
        return entry.getValue();
    }
    public T remove(String key) {
        CacheEntry<T> remove = store.remove(key);
        if (null == remove || remove.isExpired()) {
            return null;
        }
        return remove.getValue();
    }
    public boolean isEmpty() {
        optimize();
        return store.isEmpty();
    }
    public Collection<T> values() {
        optimize();
        return store.values().stream().map(CacheEntry::getValue).collect(Collectors.toList());
    }
    public void optimize() {
        store.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
    @Override
    public String toString() {
        return "CacheMap{" +
                "store=" + store +
                '}';
    }
}

