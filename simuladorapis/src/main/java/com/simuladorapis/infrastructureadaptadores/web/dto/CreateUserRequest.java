package com.simuladorapis.infrastructureadaptadores.web.dto;

import java.util.Date;

public record CreateUserRequest(
    String nombre,
    String apellidos,
    String email,
    String username,
    String password,
    Date fechaNacimiento,
    Date fechaInicioTrabajo
) {}
