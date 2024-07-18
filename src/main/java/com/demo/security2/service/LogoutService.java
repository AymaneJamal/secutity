package com.demo.security2.service;

import com.demo.security2.model.RevokedToken;
import com.demo.security2.repository.RevokedTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LogoutService {

    private final RevokedTokenRepository revokedTokenRepository;

    public LogoutService(RevokedTokenRepository revokedTokenRepository) {
        this.revokedTokenRepository = revokedTokenRepository;
    }

    public void logout(String token) {
        RevokedToken revokedToken = new RevokedToken();
        revokedToken.setToken(token);
        revokedToken.setRevokedAt(new Date());
        revokedTokenRepository.save(revokedToken);
    }
}
