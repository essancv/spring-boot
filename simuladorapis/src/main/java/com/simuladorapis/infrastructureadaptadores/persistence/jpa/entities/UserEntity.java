package com.simuladorapis.infrastructureadaptadores.persistence.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;

import com.simuladorapis.infrastructureadaptadores.persistence.jpa.entities.QuotationEntity;


import java.util.List;
import java.util.ArrayList;
import java.util.Date;


@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String nombre;
    private String apellidos;
    private String email;
    private String username;
    private String password;
    private Date fecha_nacimiento;
    private Date fecha_comienzo_trabajo;
    private boolean activo;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuotationEntity> quotations = new ArrayList<>();
    
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SimulationEntity> simulations = new ArrayList<>();

    public UserEntity (){

    }

    public long getId () {
        return this.id;
    }
    public void setId (long id) {
        this.id = id;
    }

    public String getUsername () {
        return this.username;
    }
    public void setUsername (String username) {
        this.username = username;
    }
    public String getPassword () {
        return this.password;
    }
    public void setPassword (String password) {
        this.password = password;
    }
    public String getNombre () {
        return this.nombre;
    }
    public void setNombre (String nombre) {
        this.nombre = nombre;
    }
    public String getApellidos () {
        return this.apellidos ;
    }
    public void setApellidos (String apellidos) {
        this.apellidos = apellidos;
    }
    public String getEmail () {
        return this.email;
    }
    public void setEmail (String email) {
        this.email = email;
    }
    public boolean getActivo () {
        return this.activo;
    }
    public void setActivo (boolean activo) {
        this.activo = activo;
    }

    public Date getFecha_nacimiento () {
        return this.fecha_nacimiento;
    }

    public Date getFecha_comienzo_trabajo () {
        return this.fecha_comienzo_trabajo;
    }

    public void setFecha_nacimiento ( Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public void setFecha_comienzo_trabajo ( Date fecha_comienzo_trabajo) {
        this.fecha_comienzo_trabajo = fecha_comienzo_trabajo;
    }

    public List<QuotationEntity> getQuotations() {
        return quotations;
    }

    public List<SimulationEntity> getSimulations() {
        return simulations;
    }

    public void setQuotations(List<QuotationEntity> quotations) {
        this.quotations = quotations;
    }
    public void setSimulations (List<SimulationEntity> simulations) {
        this.simulations = simulations;
    }
}