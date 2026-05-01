// AuthService.java
package com.rodriguez.inventorysales.service;

import com.rodriguez.inventorysales.dto.request.LoginRequest;
import com.rodriguez.inventorysales.dto.response.AuthResponse;
import com.rodriguez.inventorysales.entity.Usuario;
import com.rodriguez.inventorysales.repository.UsuarioRepository;
import com.rodriguez.inventorysales.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

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