package com.simuladorapis.infrastructureadaptadores.web;

import com.simuladorapis.domain.usecase.GestionarSimulacionesUseCase;
import com.simuladorapis.domain.model.Quotation;
import com.simuladorapis.domain.model.vo.SimulationId;
import com.simuladorapis.domain.model.vo.UserId;
import com.simuladorapis.infrastructureadaptadores.web.dto.CreateSimulationRequest;
import com.simuladorapis.infrastructureadaptadores.web.dto.SimulationResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/{userId}/simulations")
public class SimulationController {

    private final GestionarSimulacionesUseCase useCase;

    public SimulationController(GestionarSimulacionesUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public SimulationResponse crear(
            @PathVariable Long userId,
            @RequestBody CreateSimulationRequest request) {

        var quotations = request.quotations()
                .stream()
                .map(dto -> new Quotation(dto.amount(), dto.date()))
                .toList();

        var sim = useCase.crearSimulacion(new UserId(userId), request.name(), quotations);

        return SimulationResponse.fromDomain(sim);
    }

    @GetMapping
    public List<SimulationResponse> listar(@PathVariable Long userId) {
        return useCase.obtenerSimulaciones(new UserId(userId))
                .stream()
                .map(SimulationResponse::fromDomain)
                .toList();
    }

    @GetMapping("/{simulationId}")
    public SimulationResponse obtener(
            @PathVariable Long userId,
            @PathVariable Long simulationId) {

        var sim = useCase.obtenerSimulacion(new UserId(userId), new SimulationId(simulationId));
        return SimulationResponse.fromDomain(sim);
    }

    @DeleteMapping("/{simulationId}")
    public void borrar(
            @PathVariable Long userId,
            @PathVariable Long simulationId) {

        useCase.borrarSimulacion(new UserId(userId), new SimulationId(simulationId));
    }
}
