package com.simuladorapis.infrastructureadaptadores.web.dto;

import java.util.List;
import java.util.Date;

import com.simuladorapis.domain.model.Quotation;
import com.simuladorapis.domain.model.Simulation;


public record UserResponseDto(
    String id,
    String username,
    String nombre,
    String apellidos,
    String email,
    String password,
    String fechaNacimiento,
    String fechaInicioTrabajo,
    List<Quotation> quotations,
    List<Simulation> simulations
) {}
