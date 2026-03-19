package com.simuladorapis.infrastructureadaptadores.persistence.jpa.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.entities.ParametrosSimulacionEntity;

public interface ParametrosSimulacionJpaRepository extends JpaRepository<ParametrosSimulacionEntity, Long> {
     ParametrosSimulacionEntity findFirstByOrderByIdAsc();
}