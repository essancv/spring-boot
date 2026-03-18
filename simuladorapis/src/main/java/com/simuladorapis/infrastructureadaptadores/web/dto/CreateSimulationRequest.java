package com.simuladorapis.infrastructureadaptadores.web.dto;

import com.simuladorapis.infrastructureadaptadores.web.dto.CreateSimulationRequest;

import java.util.List;

public record CreateSimulationRequest(
        String name,
        List<CreateQuotationRequest> quotations
) {}
