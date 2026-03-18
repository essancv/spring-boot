package com.simuladorapis.domain.model.vo;


public record QuotationId(Long value) {
    public QuotationId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("QuotationId must be positive");
        }
    }
}
