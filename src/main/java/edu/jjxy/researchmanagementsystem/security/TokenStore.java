package edu.jjxy.researchmanagementsystem.security;

import edu.jjxy.researchmanagementsystem.entity.User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenStore {

    private final Map<String, User> tokenUserMap = new ConcurrentHashMap<>();

    public void put(String token, User user) {
        tokenUserMap.put(token, user);
    }

    public User get(String token) {
        return tokenUserMap.get(token);
    }

    public void remove(String token) {
        tokenUserMap.remove(token);
    }
}

