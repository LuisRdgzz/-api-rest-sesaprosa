package com.rodriguez.inventorysales.security;

import com.rodriguez.inventorysales.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .map(u -> new User(
                        u.getUsername(),
                        u.getPassword(),
                        List.of(new SimpleGrantedAuthority(u.getRole()))))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }
}