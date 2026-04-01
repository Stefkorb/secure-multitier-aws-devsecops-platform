package gr.hua.dit.kvdb.kvdb.service;

import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service

//κλάση που αποθηκέυει όλα τα ακυρωμένα tokens, οταν κάποιος χρήστης επιλέξει να κάνει log out
public class TokenBlacklistService {
    // Χρησιμοποιούμε synchronizedSet για thread-safety
    private final Set<String> blacklistedTokens = Collections.synchronizedSet(new HashSet<>());

    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}