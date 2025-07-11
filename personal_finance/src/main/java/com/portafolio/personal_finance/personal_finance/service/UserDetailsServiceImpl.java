package com.portafolio.personal_finance.personal_finance.service;

import com.portafolio.personal_finance.personal_finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Esta clase es el puente entre Spring Security y nuestra base de datos de usuarios.
 * Implementa la interfaz UserDetailsService, que Spring Security utiliza para cargar los detalles de un usuario.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    // Inyectamos nuestro repositorio para poder acceder a la base de datos.
    private final UserRepository userRepository;

    /**
     * Este es el único método que necesitamos implementar. Spring Security lo llamará
     * automáticamente durante el proceso de autenticación.
     * @param username El nombre de usuario que se está intentando autenticar.
     * @return Un objeto UserDetails (nuestra clase User ya lo implementa) si el usuario es encontrado.
     * @throws UsernameNotFoundException si el usuario no existe en la base de datos.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Usamos nuestro UserRepository para buscar al usuario por su nombre de usuario.
        // Si no lo encuentra, lanzamos una excepción que Spring Security entiende.
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el nombre de usuario: " + username));
    }
}