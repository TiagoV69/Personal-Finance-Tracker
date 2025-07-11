package com.portafolio.personal_finance.personal_finance.controller;


import com.portafolio.personal_finance.personal_finance.dto.AuthResponse;
import com.portafolio.personal_finance.personal_finance.dto.LoginRequest;
import com.portafolio.personal_finance.personal_finance.dto.RegisterRequest;
import com.portafolio.personal_finance.personal_finance.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth") // Ruta base para este controlador
@RequiredArgsConstructor
public class AuthController {

    // inyectamos nuestro servicio de logica de negocio
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {

        // Delegamos toda la logica de registro al servicio
        authService.register(request);

        // Devolvemos una respuesta HTTP 200 OK con un mensaje de éxito.
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}