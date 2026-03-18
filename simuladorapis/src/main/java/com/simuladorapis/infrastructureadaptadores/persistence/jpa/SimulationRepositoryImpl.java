
package com.simuladorapis.infrastructureadaptadores.persistence;

import com.simuladorapis.domain.model.vo.SimulationId;
import com.simuladorapis.domain.model.Simulation;
import com.simuladorapis.domain.model.vo.UserId;
import com.simuladorapis.domain.ports.SimulationRepository;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.entities.SimulationEntity;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.entities.UserEntity;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.interfaces.SimulationJpaRepository;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.mapper.SimulationMapper;
import org.springframework.context.annotation.Profile;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@Profile("jpa")
public class SimulationRepositoryImpl implements SimulationRepository {

    private final SimulationJpaRepository userRepo;

    public SimulationRepositoryImpl(SimulationJpaRepository userRepo) {
        this.userRepo = userRepo;
    }

    public Simulation save(UserId userId, Simulation simulation) {

        UserEntity user = userRepo.findById(userId.value())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        SimulationEntity entity = SimulationMapper.toEntity(simulation, user);

        user.getSimulations().add(entity);

        UserEntity saved = userRepo.save(user);

        SimulationEntity persisted = saved.getSimulations()
                .stream()
                .filter(s -> s.getName().equals(simulation.name()))
                .reduce((a, b) -> b)
                .orElseThrow();

        return SimulationMapper.toDomain(persisted);
    }

    public Optional<Simulation> findById(UserId userId, SimulationId simulationId) {
        return userRepo.findById(userId.value())
                .flatMap(u -> u.getSimulations().stream()
                        .filter(s -> s.getId().equals(simulationId.value()))
                        .findFirst()
                        .map(SimulationMapper::toDomain));
    }

    public List<Simulation> findAllByUser(UserId userId) {
        return userRepo.findById(userId.value())
                .orElseThrow()
                .getSimulations()
                .stream()
                .map(SimulationMapper::toDomain)
                .toList();
    }

    public void delete(UserId userId, SimulationId simulationId) {
        UserEntity user = userRepo.findById(userId.value())
                .orElseThrow();

        user.getSimulations().removeIf(s -> s.getId().equals(simulationId.value()));

        userRepo.save(user);
    }
}
