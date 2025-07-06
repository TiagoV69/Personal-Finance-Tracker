package com.portafolio.personal_finance.personal_finance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

//Ya los he puesto en proyectos anteriores asi que ya voy acortando documentacion... quizas 
@Data // Genera getters, setters, toString, equals y hashCode
@Builder // Implementa el patrón de diseño Builder
@NoArgsConstructor // Genera un constructor sin argumentos
@AllArgsConstructor // Genera un constructor con todos los argumentos
@Entity // Le dice a JPA que esta clase es una entidad y debe ser mapeada a una tabla
@Table(name = "users") // Especifica el nombre de la tabla en la base de datos
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;

    // --- Metodos IMPORTANTES para la interfaz UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //  Devolvemos una lista vacia.
        // Mas adelante podremos  una entidad Role y una relacion aqui.
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // La cuenta nunca expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // La cuenta nunca se bloquea
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Las credenciales nunca expiran
    }

    @Override
    public boolean isEnabled() {
        return true; // La cuenta está siempre habilitada
    }
}
