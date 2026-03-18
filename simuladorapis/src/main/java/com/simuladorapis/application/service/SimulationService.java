package com.simuladorapis.application.usecases;

import com.simuladorapis.domain.model.Simulation;
import com.simuladorapis.domain.model.Quotation;
import com.simuladorapis.domain.model.vo.SimulationId;
import com.simuladorapis.domain.model.vo.UserId;
import com.simuladorapis.domain.ports.SimulationRepository;
import com.simuladorapis.domain.usecase.GestionarSimulacionesUseCase;

import java.time.LocalDate;
import java.util.List;

import java.lang.Override;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimulationService implements  GestionarSimulacionesUseCase {

    private final SimulationRepository simulationRepository;

    @Autowired
    public SimulationService(SimulationRepository simulationRepository) {
        this.simulationRepository = simulationRepository;
    }

    @Override
    public Simulation crearSimulacion(UserId userId, String name, List<Quotation> quotations) {
        Simulation sim = new Simulation(name, LocalDate.now(), quotations);
        return simulationRepository.save(userId, sim);
    }

    @Override
    public List<Simulation> obtenerSimulaciones(UserId userId) {
        return simulationRepository.findAllByUser(userId);
    }

    @Override
    public Simulation obtenerSimulacion(UserId userId, SimulationId simulationId) {
        return simulationRepository.findById(userId, simulationId)
                .orElseThrow(() -> new IllegalArgumentException("Simulation not found"));
    }

    @Override
    public void borrarSimulacion(UserId userId, SimulationId simulationId) {
        simulationRepository.delete(userId, simulationId);
    }
}
