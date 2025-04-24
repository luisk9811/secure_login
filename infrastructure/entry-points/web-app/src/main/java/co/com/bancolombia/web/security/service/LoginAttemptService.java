package co.com.bancolombia.web.security.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 3;
    private static final int BLOCK_MINUTES = 10;

    private final Map<String, Attempt> attempts = new ConcurrentHashMap<>();

    public void loginFailed(String username) {
        Attempt attempt = attempts.getOrDefault(username, new Attempt(0, null));
        int newAttempts = attempt.count + 1;
        LocalDateTime blockTime = newAttempts >= MAX_ATTEMPTS ? LocalDateTime.now().plusMinutes(BLOCK_MINUTES) : null;
        attempts.put(username, new Attempt(newAttempts, blockTime));
    }

    public void loginSucceeded(String username) {
        attempts.remove(username);
    }

    public boolean isBlocked(String username) {
        Attempt attempt = attempts.get(username);
        if (attempt == null || attempt.blockedUntil == null) {
            return false;
        }
        if (LocalDateTime.now().isAfter(attempt.blockedUntil)) {
            attempts.remove(username);
            return false;
        }
        return true;
    }

    private record Attempt(int count, LocalDateTime blockedUntil) {
    }
}
