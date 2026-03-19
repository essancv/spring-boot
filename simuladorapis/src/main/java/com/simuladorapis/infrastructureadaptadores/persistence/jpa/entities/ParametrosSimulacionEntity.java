
package com.simuladorapis.infrastructureadaptadores.persistence.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "parametros_simulacion")
public class ParametrosSimulacionEntity {

    @Id
    private Long id;

    private BigDecimal baseMinimaCotizacion;
    private Integer aniosSimular;
    private BigDecimal pensionMaxima;

    // getters/setters

    public BigDecimal getBaseMinimaCotizacion () { return this.baseMinimaCotizacion ;}
    public Integer getAniosSimular () { return this.aniosSimular ;}
    public BigDecimal getPensionMaxima () { return this.pensionMaxima ;}
}