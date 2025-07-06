package com.portafolio.personal_finance.personal_finance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portafolio.personal_finance.personal_finance.entity.User;

// La anotacion @Repository le indica a Spring que esta es una interfaz de repositorio
// y la considera un Bean para poder inyectarla en otras clases
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA generara automaticamente la consulta SQL para este metodo:
    // "SELECT u FROM User u WHERE u.username = :username" siendo mas claros
    Optional<User> findByUsername(String username);

}
