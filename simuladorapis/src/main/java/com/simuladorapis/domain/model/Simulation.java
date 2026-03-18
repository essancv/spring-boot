package com.simuladorapis.domain.model;

import com.simuladorapis.domain.model.vo.SimulationId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Simulation {

    private SimulationId id;
    private String name;
    private LocalDate createdAt;
    private List<Quotation> quotations;

    // Constructor para creación
    public Simulation(String name, LocalDate createdAt, List<Quotation> quotations) {
        this.name = name;
        this.createdAt = createdAt;
        this.quotations = quotations != null ? quotations : new ArrayList<>();
    }

    // Constructor para reconstrucción
    public Simulation(SimulationId id, String name, LocalDate createdAt, List<Quotation> quotations) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.quotations = quotations != null ? quotations : new ArrayList<>();
    }

    public SimulationId id() { return id; }
    public String name() { return name; }
    public LocalDate createdAt() { return createdAt; }
    public List<Quotation> quotations() { return quotations; }

    // Métodos de negocio
    public void addQuotation(Quotation q) {
        quotations.add(q);
    }

    public void removeQuotation(Long quotationId) {
        quotations.removeIf(q -> q.getId().value().equals(quotationId));
    }

    public void clearQuotations() {
        quotations.clear();
    }

    // TODO - Revisar como sustituir con DTO en los mapper
    public SimulationId getId() { return id; }
    public String getName() { return name; }
    public LocalDate getCreatedAt() { return createdAt; }
    public List<Quotation> getQuotations() { return quotations; }
}
