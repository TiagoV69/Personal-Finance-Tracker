package com.portafolio.personal_finance.personal_finance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Le dice a Spring que esta clase contiene configuraciones y beans
public class SecurityConfig {

    @Bean // Le dice a Spring que este metodo produce un bean para ser gestionado por el contenedor de Spring
    public PasswordEncoder passwordEncoder() {
        // Usamos BCrypt el predeterminado de la industria para el hashing de contraseñas
        return new BCryptPasswordEncoder();
    }

       @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Deshabilitar CSRF (Cross-Site Request Forgery) porque usaremos JWT (stateless)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Configurar las reglas de autorización de las peticiones HTTP
                .authorizeHttpRequests(auth -> auth
                        // Permite el acceso sin autenticación a todos los endpoints bajo /api/auth/
                        .requestMatchers("/api/auth/**").permitAll()
                        // Para cualquier otra petición, requiere autenticación
                        .anyRequest().authenticated()
                )

                // 3. Configurar la gestión de sesiones como STATELESS (sin estado)
                // Spring Security no creará ni usará HttpSession. Cada petición debe ser aut_contenticada.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
