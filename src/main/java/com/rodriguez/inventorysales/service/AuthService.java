package com.rodriguez.inventorysales.service;

import com.rodriguez.inventorysales.dto.request.LoginRequest;
import com.rodriguez.inventorysales.dto.response.AuthResponse;
import com.rodriguez.inventorysales.entity.Usuario;
import com.rodriguez.inventorysales.repository.UsuarioRepository;
import com.rodriguez.inventorysales.security.JwtService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       UserDetailsService userDetailsService,
                       UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()));
        UserDetails ud = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(ud);
        return new AuthResponse(token, ud.getUsername());
    }

    @PostConstruct
    public void initDefaultUser() {
        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            Usuario admin = Usuario.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role("ROLE_ADMIN")
                    .build();
            usuarioRepository.save(admin);
            log.info("Usuario admin inicial creado: admin / admin123");
        }
    }
}