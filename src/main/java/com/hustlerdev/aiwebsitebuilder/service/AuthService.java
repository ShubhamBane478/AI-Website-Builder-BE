package com.hustlerdev.aiwebsitebuilder.service;

import com.hustlerdev.aiwebsitebuilder.dto.request.RegisterRequest;
import com.hustlerdev.aiwebsitebuilder.dto.response.AuthResponse;
import com.hustlerdev.aiwebsitebuilder.dto.response.UserDto;
import com.hustlerdev.aiwebsitebuilder.entity.RefreshToken;
import com.hustlerdev.aiwebsitebuilder.entity.User;
import com.hustlerdev.aiwebsitebuilder.exception.EmailAlreadyExistsException;
import com.hustlerdev.aiwebsitebuilder.repository.RefreshTokenRepository;
import com.hustlerdev.aiwebsitebuilder.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthResponse register(RegisterRequest request, HttpServletResponse response) {

        // 1. Check email is not already taken
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        // 2. Build the User entity
        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))  // never store plain password
                .sitesPublished(0)
                .sitesLimit(3)
                .isAdmin(false)
                .build();

        // 3. Save user to database
        User savedUser = userRepository.save(user);

        // 4. Generate access token (JWT)
        String accessToken = jwtService.generateAccessToken(savedUser);

        // 5. Generate refresh token and save to DB
        RefreshToken refreshToken = jwtService.generateRefreshToken(savedUser);
        refreshTokenRepository.save(refreshToken);

        // 6. Set refresh token as httpOnly cookie
        Cookie cookie = new Cookie("refreshToken", refreshToken.getToken());
        cookie.setHttpOnly(true);   // JS cannot read this — protects against XSS
        cookie.setSecure(true);     // only sent over HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days in seconds
        response.addCookie(cookie);

        // 7. Build and return response (no passwordHash!)
        return AuthResponse.builder()
                .accessToken(accessToken)
                .user(UserDto.builder()
                        .id(savedUser.getId())
                        .email(savedUser.getEmail())
                        .sitesPublished(savedUser.getSitesPublished())
                        .sitesLimit(savedUser.getSitesLimit())
                        .build())
                .build();
    }
}
