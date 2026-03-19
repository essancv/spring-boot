package com.simuladorapis.infrastructureadaptadores.persistence;

import com.simuladorapis.domain.model.ParametrosSimulacion;
import com.simuladorapis.domain.ports.ParametrosSimulacionRepository;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.interfaces.ParametrosSimulacionJpaRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;


@Repository
@Profile("jpa")
public class ParametrosSimulacionRepositoryImpl implements ParametrosSimulacionRepository {

    private final ParametrosSimulacionJpaRepository configurationRepo;

    public ParametrosSimulacionRepositoryImpl(ParametrosSimulacionJpaRepository configurationRepo) {
        this.configurationRepo = configurationRepo;
    }

    @Override
    public ParametrosSimulacion leeParametrosSimulacion() {
        var entity = configurationRepo.findFirstByOrderByIdAsc();
        return new ParametrosSimulacion(
            entity.getBaseMinimaCotizacion(),
            entity.getAniosSimular(),
            entity.getPensionMaxima()
        );
    }
}