package com.simuladorapis.domain.ports;

import com.simuladorapis.domain.model.Simulation;
import com.simuladorapis.domain.model.vo.SimulationId;
import com.simuladorapis.domain.model.vo.UserId;

import java.util.List;
import java.util.Optional;

public interface SimulationRepository {

    Simulation save(UserId userId, Simulation simulation);

    Optional<Simulation> findById(UserId userId, SimulationId simulationId);

    List<Simulation> findAllByUser(UserId userId);

    void delete(UserId userId, SimulationId simulationId);
}
