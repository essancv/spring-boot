package com.simuladorapis.infrastructureadaptadores.persistence.jpa;

import com.simuladorapis.domain.model.User;
import com.simuladorapis.domain.ports.SimulationQuotationRepository;
import org.springframework.stereotype.Repository;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.entities.UserEntity;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.entities.QuotationEntity;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.entities.SimulationEntity;
import com.simuladorapis.domain.model.vo.UserId;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.interfaces.SimulationQuotationJpaRepository;
import org.springframework.context.annotation.Profile;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.mapper.UserMapper;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.mapper.QuotationMapper;
import com.simuladorapis.domain.model.vo.SimulationId;
import com.simuladorapis.domain.model.Quotation;


@Repository
@Profile("jpa")
public class SimulationQuotationRepositoryImpl implements SimulationQuotationRepository {

    private final SimulationQuotationJpaRepository userRepo;

    public SimulationQuotationRepositoryImpl(SimulationQuotationJpaRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Quotation save(UserId userId, SimulationId simId, Quotation quotation) {

        UserEntity user = userRepo.findById(userId.value())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        SimulationEntity sim = user.getSimulations().stream()
                .filter(s -> s.getId().equals(simId.value()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Simulation not found"));

        QuotationEntity entity = QuotationMapper.toEntityForSimulation(quotation, sim);

        sim.getQuotations().add(entity);

        UserEntity saved = userRepo.save(user);

        SimulationEntity persistedSim = saved.getSimulations().stream()
                .filter(s -> s.getId().equals(simId.value()))
                .findFirst()
                .orElseThrow();

        QuotationEntity persisted = persistedSim.getQuotations().stream()
                .filter(q -> q.getAmount().equals(quotation.getAmount())
                          && q.getDate().equals(quotation.getDate()))
                .reduce((a, b) -> b)
                .orElseThrow();

        return QuotationMapper.toDomain(persisted);
    }
}
