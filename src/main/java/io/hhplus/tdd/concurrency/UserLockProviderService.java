package io.hhplus.tdd.concurrency;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class UserLockProviderService {
    private final Map<Long, ReentrantLock> userLockMap;
    private final static Long initialCapacity = 10L;
    UserLockProviderService() {
        this.userLockMap = new ConcurrentHashMap<>();
        for(int i=0; i<initialCapacity; i++) {
            this.userLockMap.put(Long.valueOf(i), new ReentrantLock(true));
        }
    }

    private synchronized ReentrantLock getUserLock(Long userId) {
        return this.userLockMap.get(userId % initialCapacity);
    }

    public void lock(Long userId) {
        this.getUserLock(userId).lock();
    }

    public void unlock(Long userId) {
        this.getUserLock(userId).unlock();
    }
}
