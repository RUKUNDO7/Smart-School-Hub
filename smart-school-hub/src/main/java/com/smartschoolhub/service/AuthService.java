package com.smartschoolhub.service;

import com.smartschoolhub.domain.UserAccount;
import com.smartschoolhub.service.dto.AuthResponse;
import com.smartschoolhub.service.dto.LoginRequest;
import com.smartschoolhub.service.dto.RegisterRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserAccountService userAccountService;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, UserAccountService userAccountService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userAccountService = userAccountService;
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserAccount user = (UserAccount) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getUsername(), user.getRole().name());
    }

    public AuthResponse register(RegisterRequest request) {
        UserAccount user = userAccountService.createUser(request);
        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getUsername(), user.getRole().name());
    }
}
