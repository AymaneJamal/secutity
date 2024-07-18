package com.demo.security2.repository;

import com.demo.security2.model.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RevokedTokenRepository extends MongoRepository<RevokedToken, Long> {
    Optional<RevokedToken> findByToken(String token);
}
