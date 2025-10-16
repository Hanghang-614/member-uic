/*
 * Copyright 2024. Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 *
 */

package com.wch.member.error;

/**
 * @author anshi
 * @date: 2024/12/26 16:32
 */
public class CacheEntry<T> {
    private T value;
    private final long expirationTime;

    /**
     *
     * @param value
     * @param duration ms, 0: never
     */
    public CacheEntry(T value, long duration) {
        if (null == value || duration < 0) {
            throw new IllegalArgumentException();
        }

        this.value = value;

        if (duration == 0) {
            this.expirationTime = Long.MAX_VALUE;
        } else {
            this.expirationTime = System.currentTimeMillis() + duration;
        }
    }

    public T getValue() {
        return value;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expirationTime;
    }

    @Override
    public String toString() {
        return "CacheEntry{" +
            "value=" + value +
            ", expirationTime=" + expirationTime +
            '}';
    }
}
