package com.portafolio.personal_finance.personal_finance.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.portafolio.personal_finance.personal_finance.dto.RegisterRequest;
import com.portafolio.personal_finance.personal_finance.entity.User;
import com.portafolio.personal_finance.personal_finance.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service // Marca esta clase como un servicio de negocio en la capa de servicio
@RequiredArgsConstructor // Anotacion de Lombok que genera un constructor con los campos final
public class AuthService {

    // Inyeccion de dependencias a traves del constructor esto por el @RequiredArgsConstructor
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
}   
