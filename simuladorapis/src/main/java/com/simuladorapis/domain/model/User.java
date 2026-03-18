package com.simuladorapis.domain.model;

import com.simuladorapis.domain.model.vo.UserId;
import com.simuladorapis.domain.model.Quotation;
import com.simuladorapis.domain.model.Simulation;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class User {
    private UserId id;
    private String nombre;
    private String apellidos;
    private String email;
    private String username;
    private String password;
    private boolean activo;
    private Date fecha_nacimiento;
    private Date fecha_comienzo_trabajo;
    private List<Quotation> quotations;
    private List<Simulation> simulations;

    // Getters y setters

     // Constructor para nuevos usuarios
    public User ( String username, String nombre , String apellidos  , String email , String password,Date fecha_nacimiento, Date fecha_comienzo_trabajo) {
        System.out.println (" Estamos en el constrctor de USER para nuevo usuario " + username);
       if (username == null || username.isBlank())
            throw new IllegalArgumentException("Username cannot be empty");

        this.username = username;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.password = password;
        this.password = password;
        this.fecha_nacimiento = fecha_nacimiento;
        this.activo = true;
        this.quotations = new ArrayList<>();
        this.simulations = new ArrayList<>();
    }

    // Constructor para crear el usuario desde persistencia
    public User ( UserId id  , String username, String nombre , String apellidos  , String email , String password , Date fecha_nacimiento, Date fecha_comienzo_trabajo, boolean activo , List quotations , List simulations ) {
        if (id == null) throw new IllegalArgumentException("UserId cannot be null");
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username cannot be empty");

        this.id = id;
        this.username = username;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.password = password;
        this.fecha_nacimiento = fecha_nacimiento;
        this.fecha_comienzo_trabajo = fecha_comienzo_trabajo;
        this.activo = activo;
        this.quotations = quotations != null ? quotations : new ArrayList<>();
        this.simulations = simulations != null ? simulations : new ArrayList<>();
    }

    public UserId getId () {
        return this.id;
    }
    public String getUsername () {
        return this.username;
    }
    public Date getFecha_nacimiento () {
        return this.fecha_nacimiento;
    }
    public Date getFecha_comienzo_trabajo () {
        return this.fecha_comienzo_trabajo;
    }
    public void setFecha_nacimiento (Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }
    public void setFecha_comienzo_trabajo (Date fecha_comienzo_trabajo) {
        this.fecha_comienzo_trabajo = fecha_comienzo_trabajo;
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

    public List<Quotation> getQuotations () {
        return this.quotations;
    }
    public List<Simulation> getSimulations () {
        return this.simulations;
    }
}