package com.simuladorapis.infrastructureadaptadores.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateQuotationRequest(
        BigDecimal amount,
        String year,
        String month
       
) {
        public LocalDate date() {
                 return LocalDate.of(Integer.parseInt (year), Integer.parseInt (month), 1);
    }
}
