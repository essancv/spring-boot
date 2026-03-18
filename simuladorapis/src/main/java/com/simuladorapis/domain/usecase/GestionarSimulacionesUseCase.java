package com.simuladorapis.domain.usecase;

import com.simuladorapis.domain.model.Simulation;
import com.simuladorapis.domain.model.Quotation;
import com.simuladorapis.domain.model.vo.SimulationId;
import com.simuladorapis.domain.model.vo.UserId;
import com.simuladorapis.domain.ports.SimulationRepository;

import java.time.LocalDate;
import java.util.List;

public interface GestionarSimulacionesUseCase {

    public Simulation crearSimulacion(UserId userId, String name, List<Quotation> quotations);

    public List<Simulation> obtenerSimulaciones(UserId userId);

    public Simulation obtenerSimulacion(UserId userId, SimulationId simulationId) ;

    public void borrarSimulacion(UserId userId, SimulationId simulationId) ;
}
