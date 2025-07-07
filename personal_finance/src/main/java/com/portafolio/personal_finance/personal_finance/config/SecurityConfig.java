package com.portafolio.personal_finance.personal_finance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // Le dice a Spring que esta clase contiene configuraciones y beans
public class SecurityConfig {

    @Bean // Le dice a Spring que este metodo produce un bean para ser gestionado por el contenedor de Spring
    public PasswordEncoder passwordEncoder() {
        // Usamos BCrypt el predeterminado de la industria para el hashing de contraseñas
        return new BCryptPasswordEncoder();
    }
}
