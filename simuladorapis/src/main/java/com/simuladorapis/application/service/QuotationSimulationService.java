package com.simuladorapis.application.service;

import com.simuladorapis.domain.model.Quotation;

import com.simuladorapis.domain.model.Simulation;
import com.simuladorapis.domain.model.vo.QuotationId;
import com.simuladorapis.domain.model.vo.UserId;
import com.simuladorapis.domain.model.vo.SimulationId;
import com.simuladorapis.domain.ports.SimulationRepository;
import com.simuladorapis.domain.usecase.GestionarCotizacionesSimulacionUseCase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.lang.Override;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class QuotationSimulationService implements GestionarCotizacionesSimulacionUseCase {

    private final SimulationRepository simulationRepository;

    @Autowired
    public QuotationSimulationService(SimulationRepository simulationRepository) {
        this.simulationRepository = simulationRepository;
    }

    @Override
    public Quotation crearCotizacion(UserId userId, SimulationId simulationId,
                                     BigDecimal amount, LocalDate date) {

        Simulation sim = simulationRepository.findById(userId, simulationId)
                .orElseThrow(() -> new IllegalArgumentException("Simulation not found"));

        Quotation q = new Quotation(amount, date);
        sim.addQuotation(q);

        simulationRepository.save(userId, sim);

        return q;
    }

    @Override
    public List<Quotation> obtenerCotizaciones(UserId userId, SimulationId simulationId) {
        return simulationRepository.findById(userId, simulationId)
                .orElseThrow(() -> new IllegalArgumentException("Simulation not found"))
                .quotations();
    }

    @Override
    public Quotation modificarCotizacion(UserId userId, SimulationId simulationId,
                                         QuotationId quotationId,
                                         BigDecimal amount, LocalDate date) {

        Simulation sim = simulationRepository.findById(userId, simulationId)
                .orElseThrow(() -> new IllegalArgumentException("Simulation not found"));

        Quotation q = sim.quotations().stream()
                .filter(x -> x.getId().value().equals(quotationId.value()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Quotation not found"));

        q.update(amount, date);

        simulationRepository.save(userId, sim);

        return q;
    }

    @Override
    public void borrarCotizacion(UserId userId, SimulationId simulationId, QuotationId quotationId) {
        Simulation sim = simulationRepository.findById(userId, simulationId)
                .orElseThrow(() -> new IllegalArgumentException("Simulation not found"));

        sim.removeQuotation(quotationId.value());

        simulationRepository.save(userId, sim);
    }

    @Override
    public void borrarTodas(UserId userId, SimulationId simulationId) {
        Simulation sim = simulationRepository.findById(userId, simulationId)
                .orElseThrow(() -> new IllegalArgumentException("Simulation not found"));

        sim.clearQuotations();

        simulationRepository.save(userId, sim);
    }
}
