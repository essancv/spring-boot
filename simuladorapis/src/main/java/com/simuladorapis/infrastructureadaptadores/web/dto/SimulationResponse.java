package com.simuladorapis.infrastructureadaptadores.web.dto;

import com.simuladorapis.infrastructureadaptadores.web.dto.QuotationResponse;
import com.simuladorapis.domain.model.Simulation;

import java.util.List;
import java.time.LocalDate;

public record SimulationResponse(
        Long id,
        String name,
        LocalDate createdAt,
        List<QuotationResponse> quotations
) {
    public static SimulationResponse fromDomain(Simulation sim) {
        return new SimulationResponse(
                sim.id() != null ? sim.id().value() : null,
                sim.name(),
                sim.createdAt(),
                sim.quotations().stream()
                        .map(QuotationResponse::fromDomain)
                        .toList()
        );
    }
}
