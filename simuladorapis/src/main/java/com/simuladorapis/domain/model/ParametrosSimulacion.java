package com.simuladorapis.domain.model;

import java.math.BigDecimal;

public class ParametrosSimulacion {
    private final BigDecimal baseMinimaCotizacion;
    private final int aniosSimular;
    private final BigDecimal pensionMaxima;

    // constructor (Vendrá de BBDD)

    
    public ParametrosSimulacion (BigDecimal baseMinimaCotizacion , int aniosSimular , BigDecimal pensionMaxima) {
        this.baseMinimaCotizacion = baseMinimaCotizacion;
        this.aniosSimular = aniosSimular;
        this.pensionMaxima = pensionMaxima;
    }

        
    public BigDecimal getBaseMinimaCotizacion () { return this.baseMinimaCotizacion ;}
    public int getAniosSimular () { return this.aniosSimular ;}
    public BigDecimal getPensionMaxima () { return this.pensionMaxima ;}

}