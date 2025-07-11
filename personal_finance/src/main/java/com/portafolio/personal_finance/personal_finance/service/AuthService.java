package com.portafolio.personal_finance.personal_finance.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.portafolio.personal_finance.personal_finance.dto.AuthResponse;
import com.portafolio.personal_finance.personal_finance.dto.LoginRequest;
import com.portafolio.personal_finance.personal_finance.dto.RegisterRequest;
import com.portafolio.personal_finance.personal_finance.entity.User;
import com.portafolio.personal_finance.personal_finance.repository.UserRepository;
import com.portafolio.personal_finance.personal_finance.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service // Marca esta clase como un servicio de negocio en la capa de servicio
@RequiredArgsConstructor // Anotacion de Lombok que genera un constructor con los campos final
public class AuthService {

    // Inyeccion de dependencias a traves del constructor esto por el @RequiredArgsConstructor
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService; // Inyectar JwtService
    private final AuthenticationManager authenticationManager; // Inyectar AuthenticationManager

    public void register(RegisterRequest request) {
        // para validad si el usuario existe
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Intenta nuevamente, el usuario se encuentra en uso.");
        }

        // Creamos la entidad User a partir del DTO
            User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // esto es IMPORTANTE se hashea la contraseña antes de guardarla
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        // Guardamos el nuevo usuario en la base de datos
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserDetails user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.generateToken(user);
        return AuthResponse.builder().token(token).build();
    }
}   
