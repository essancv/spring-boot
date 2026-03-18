package com.simuladorapis.infrastructureadaptadores.web;

import com.simuladorapis.domain.usecase.GestionarCotizacionesSimulacionUseCase;
import com.simuladorapis.domain.model.vo.SimulationId;
import com.simuladorapis.domain.model.vo.QuotationId;
import com.simuladorapis.domain.model.vo.UserId;
import com.simuladorapis.infrastructureadaptadores.web.dto.CreateQuotationRequest;
import com.simuladorapis.infrastructureadaptadores.web.dto.UpdateQuotationRequest;
import com.simuladorapis.infrastructureadaptadores.web.dto.QuotationResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/{userId}/simulations/{simulationId}/quotations")
public class SimulationQuotationController {

    private final GestionarCotizacionesSimulacionUseCase useCase;

    public SimulationQuotationController(GestionarCotizacionesSimulacionUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public QuotationResponse crear(
            @PathVariable Long userId,
            @PathVariable Long simulationId,
            @RequestBody CreateQuotationRequest request) {

        var quotation = useCase.crearCotizacion(
                new UserId(userId),
                new SimulationId(simulationId),
                request.amount(),
                request.date()
        );

        return QuotationResponse.fromDomain(quotation);
    }

    @GetMapping
    public List<QuotationResponse> listar(
            @PathVariable Long userId,
            @PathVariable Long simulationId) {

        return useCase.obtenerCotizaciones(
                new UserId(userId),
                new SimulationId(simulationId)
        ).stream().map(QuotationResponse::fromDomain).toList();
    }

    @PutMapping("/{quotationId}")
    public QuotationResponse modificar(
            @PathVariable Long userId,
            @PathVariable Long simulationId,
            @PathVariable Long quotationId,
            @RequestBody UpdateQuotationRequest request) {

        var quotation = useCase.modificarCotizacion(
                new UserId(userId),
                new SimulationId(simulationId),
                new QuotationId(quotationId),
                request.amount(),
                request.date()
        );

        return QuotationResponse.fromDomain(quotation);
    }

    @DeleteMapping("/{quotationId}")
    public void borrar(
            @PathVariable Long userId,
            @PathVariable Long simulationId,
            @PathVariable Long quotationId) {

        useCase.borrarCotizacion(
                new UserId(userId),
                new SimulationId(simulationId),
                new QuotationId(quotationId)
        );
    }

    @DeleteMapping
    public void borrarTodas(
            @PathVariable Long userId,
            @PathVariable Long simulationId) {

        useCase.borrarTodas(
                new UserId(userId),
                new SimulationId(simulationId)
        );
    }
}
