package com.portafolio.personal_finance.personal_finance.config;

import com.portafolio.personal_finance.personal_finance.service.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



/**
 * Clase de configuración central para toda la seguridad de la aplicación.
 * Reúne todas las piezas necesarias para la autenticación y autorización.
 */

@Configuration // Marca esta clase como una fuente de definiciones de Beans. Spring la escaneará para encontrar los métodos anotados con @Bean.
@EnableWebSecurity // Habilita la integración de Spring Security con Spring MVC.
@RequiredArgsConstructor // Anotación de Lombok que genera un constructor con todos los campos finales (final)
public class SecurityConfig {

    /**
     * Inyección de nuestro servicio personalizado de detalles de usuario.
     * Spring buscará un Bean de tipo UserDetailsServiceImpl (que es nuestro UserDetailsServiceImpl
     * con la anotación @Service) y lo pasará al constructor de esta clase.
     */
    private final UserDetailsServiceImpl userDetailsService;

    // --- BEANS DE AUTENTICACIÓN ---

    /**
     * Define el "Proveedor de Autenticación". Este es el componente que realmente
     * procesa la autenticación. Le enseñamos a usar nuestro servicio de usuarios
     * y nuestro codificador de contraseñas.
     * @return una instancia configurada de AuthenticationProvider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // DaoAuthenticationProvider es la implementación estándar que utiliza un UserDetailsService
        // y un PasswordEncoder para autenticar a un usuario.
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        // Le asignamos nuestro servicio de usuarios personalizado.
        authProvider.setUserDetailsService(userDetailsService);

        // Le asignamos el codificador de contraseñas que usaremos.
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Expone el AuthenticationManager de Spring Security como un Bean.
     * El AuthenticationManager es el orquestador principal del proceso de autenticación.
     * Lo necesitaremos en nuestro AuthService para procesar la petición de login.
     * @param config La configuración de autenticación por defecto de Spring.
     * @return El AuthenticationManager gestionado por Spring.
     * @throws Exception si hay un error al obtener el manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Define el Bean para la codificación de contraseñas.
     * Usamos BCrypt, que es el estándar de facto, ya que es fuerte y lento a propósito,
     * dificultando los ataques de fuerza bruta.
     * @return una instancia de PasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // --- BEAN DE CONFIGURACIÓN DE LAS REGLAS HTTP ---

    /**
     * Define la cadena de filtros de seguridad. Aquí es donde configuramos las reglas
     * de "quién puede acceder a qué". Es el corazón de la autorización.
     * @param http el objeto HttpSecurity para configurar.
     * @return la cadena de filtros de seguridad construida.
     * @throws Exception si hay un error en la configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitar CSRF (Cross-Site Request Forgery).
                // No lo necesitamos para una API RESTful stateless que usa tokens JWT.
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                    // Permite el acceso público a nuestros endpoints de autenticación
                .requestMatchers("/api/auth/**").permitAll()
                    // Para cualquier otra petición, requiere autenticación
                .anyRequest().authenticated()
                )
                // Le decimos a HttpSecurity que use nuestro proveedor de autenticación personalizado
                .authenticationProvider(authenticationProvider());

        return http.build();
    }
}