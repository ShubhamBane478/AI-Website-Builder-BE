package com.hustlerdev.aiwebsitebuilder.repository;

import com.hustlerdev.aiwebsitebuilder.entity.RefreshToken;
import com.hustlerdev.aiwebsitebuilder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    // Find by the token string (used when client sends refresh cookie)
    Optional<RefreshToken> findByToken(String token);

    // Delete all tokens for a user (used on logout)
    void deleteByUser(User user);
}
