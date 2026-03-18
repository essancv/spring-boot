package com.simuladorapis.domain.ports;

import com.simuladorapis.domain.model.User;
import java.util.Optional;

public interface UserRepository {
    void guardar(User user);  //Alta
    void eliminar(String user);  //Baja
    void actualizar(User user); //Modificacion
    User obtener(String id);  //Lectura (con id)
    Optional<User> findByUsername(String username);
}