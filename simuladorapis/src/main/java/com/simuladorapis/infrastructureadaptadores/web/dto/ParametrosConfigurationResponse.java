package com.simuladorapis.infrastructureadaptadores.web.dto;

import com.simuladorapis.domain.model.ParametrosSimulacion;

import java.math.BigDecimal;

public record ParametrosConfigurationResponse (
    BigDecimal  baseMinimaCotizacion,
    int     aniosSimular,
    BigDecimal  pensionMaxima
) {

    public static ParametrosConfigurationResponse from(ParametrosSimulacion params) {
        return new ParametrosConfigurationResponse(
            params.getBaseMinimaCotizacion(),
            params.getAniosSimular(),
            params.getPensionMaxima()
        );
    }
}
