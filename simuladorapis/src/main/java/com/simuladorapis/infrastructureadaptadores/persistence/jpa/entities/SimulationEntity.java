package com.simuladorapis.infrastructureadaptadores.persistence.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.entities.SimulationEntity;


import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "simulations")
public class SimulationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "simulation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuotationEntity> quotations = new ArrayList<>();

    // getters y setters

    public Long getId () { return this.id;}
    public void setId (Long id) {  this.id = id;}
    public String getName () { return this.name;}
    public void setName (String name) {  this.name = name;}
    public LocalDate getCreatedAt () { return this.createdAt;}
    public void setCreatedAt (LocalDate createdAt) {  this.createdAt =createdAt;}
    public List<QuotationEntity>  getQuotations () { return this.quotations;}
    public void  setQuotations ( List<QuotationEntity> quotations) {  this.quotations =quotations;}
    public void setUser (UserEntity user) {  this.user = user;}
    public UserEntity getUser () {  return this.user ;}
}
