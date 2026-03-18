package com.simuladorapis.infrastructureadaptadores.persistence.jpa.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.entities.UserEntity;

public interface SimulationJpaRepository extends JpaRepository<UserEntity, Long> {
    // Métodos adicionales si es necesario
}
