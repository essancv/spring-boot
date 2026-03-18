package com.simuladorapis.domain.usecase;

import com.simuladorapis.domain.model.User;
import com.simuladorapis.infrastructureadaptadores.web.dto.UpdateUserRequest;

import java.util.Date;

public interface GestionarUsersUseCase {
    void darDeAltaUser(String username, String nombre , String apellidos  , String email , String password, Date fecha_nacimiento,Date fecha_comienzo_trabajo);
    void darDeBajaUser(String user);
    void modificarUser(String  id , UpdateUserRequest dto );
    User obtenerUser(String user);
}