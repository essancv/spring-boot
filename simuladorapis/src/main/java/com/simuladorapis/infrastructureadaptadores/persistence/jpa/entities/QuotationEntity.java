package com.simuladorapis.infrastructureadaptadores.persistence.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import com.simuladorapis.infrastructureadaptadores.persistence.jpa.entities.SimulationEntity;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "quotations")
public class QuotationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal amount;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    // Cotización dentro de una simulación
    @ManyToOne
    @JoinColumn(name = "simulation_id", nullable = true)
    private SimulationEntity simulation;

    public QuotationEntity ( ) {}
    public QuotationEntity (Long id , BigDecimal amount, LocalDate date) {
        this.id = id;
        this.amount = amount;
        this.date = date;
    }
    public Long getId () { return this.id;}
    public BigDecimal getAmount () { return this.amount;}
    public LocalDate getDate () { return this.date;}

    public void setId ( Long id ) { this.id = id;}
    public void setAmount ( BigDecimal amount ) { this.amount = amount;}
    public void setDate ( LocalDate date ) { this.date = date;}
    public void setUser (UserEntity user) { this.user = user;}
    public void setSimulation (SimulationEntity sim) { this.simulation = sim;}
}
