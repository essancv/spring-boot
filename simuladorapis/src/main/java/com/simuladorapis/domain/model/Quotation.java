package com.simuladorapis.domain.model;

import com.simuladorapis.domain.model.vo.QuotationId;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Quotation {
    private  QuotationId id;
    private BigDecimal amount;
    private LocalDate date;


    /* Constructor WEB */
    public Quotation ( BigDecimal amount , LocalDate date) {
        this.amount = amount;
        this.date = date;
    }

    /* Constructor REPO*/

    public Quotation (QuotationId id , BigDecimal amount , LocalDate date) {
        this.id = id;
        this.amount = amount;
        this.date = date;
    }
    /* getters and setters */

    public QuotationId getId () { return this.id;}
    public BigDecimal getAmount () { return this.amount;}
    public LocalDate getDate () { return this.date;}

    public void update (BigDecimal amount , LocalDate date ) {
        this.amount = amount;
        this.date = date;
    }
}
