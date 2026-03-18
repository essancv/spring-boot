package com.simuladorapis.infrastructureadaptadores.persistence.jpa.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.entities.UserEntity;
import java.util.Optional;
import com.simuladorapis.domain.model.User;

public interface UserJpaRepository extends JpaRepository<UserEntity, String> {
    // Métodos adicionales si es necesario
     public Optional<UserEntity> findByUsername(String username);
}
