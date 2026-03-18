package com.simuladorapis.infrastructureadaptadores.web.dto;

import com.simuladorapis.domain.model.Quotation;

import java.math.BigDecimal;
import java.time.LocalDate;

public record QuotationResponse(
        Long id,
        BigDecimal amount,
        LocalDate date
) {
    public static QuotationResponse fromDomain(Quotation q) {
        return new QuotationResponse(
                q.getId().value(),
                q.getAmount(),
                q.getDate()
        );
    }
}
