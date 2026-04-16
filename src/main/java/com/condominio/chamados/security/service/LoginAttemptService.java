package com.condominio.chamados.security.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {

    private final Map<String, Integer> attempts = new ConcurrentHashMap<>();
    private final Map<String, Instant> lockTime = new ConcurrentHashMap<>();

    private final int MAX_ATTEMPTS = 5;
    private final Duration LOCK_DURATION = Duration.ofMinutes(15);

    public void loginSucceeded(String key) {
        attempts.remove(key);
        lockTime.remove(key);
    }

    public void loginFailed(String key) {
        if (isBlocked(key)) {
            return; // already blocked
        }
        int current = attempts.getOrDefault(key, 0);
        current++;
        attempts.put(key, current);
        if (current >= MAX_ATTEMPTS) {
            lockTime.put(key, Instant.now());
        }
    }

    public boolean isBlocked(String key) {
        Instant lockedAt = lockTime.get(key);
        if (lockedAt == null) return false;
        if (Instant.now().isAfter(lockedAt.plus(LOCK_DURATION))) {
            // lock expired
            lockTime.remove(key);
            attempts.remove(key);
            return false;
        }
        return true;
    }

    public long getRemainingLockSeconds(String key) {
        Instant lockedAt = lockTime.get(key);
        if (lockedAt == null) return 0;
        long seconds = LOCK_DURATION.minus(Duration.between(lockedAt, Instant.now())).getSeconds();
        return Math.max(0, seconds);
    }
}
