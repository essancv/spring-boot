package com.simuladorapis.domain.model.vo;

public record SimulationId(Long value) {
    public SimulationId {
        if (value == null || value <= 0)
            throw new IllegalArgumentException("SimulationId must be positive");
    }
}
