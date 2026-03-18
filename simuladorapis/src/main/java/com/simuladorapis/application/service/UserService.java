package com.simuladorapis.application.service;

import com.simuladorapis.domain.usecase.GestionarUsersUseCase;
import com.simuladorapis.domain.ports.UserRepository;
import com.simuladorapis.domain.model.User;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.lang.Override;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.simuladorapis.infrastructureadaptadores.web.dto.UpdateUserRequest;

import java.util.Date;

@Service
public class UserService implements GestionarUsersUseCase {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public void darDeAltaUser(String username, String nombre , String apellidos  , String email , String password , Date fecha_nacimiento,Date fecha_comienzo_trabajo) {
         String encodedPassword = passwordEncoder.encode(password);
         User user = new User(username, nombre , apellidos , email, encodedPassword,fecha_nacimiento,fecha_comienzo_trabajo);
        userRepository.guardar(user);
    }
    
    @Override
    public void darDeBajaUser(String user) {
        userRepository.eliminar(user);
    }
    
    @Override
    public void modificarUser(String id , com.simuladorapis.infrastructureadaptadores.web.dto.UpdateUserRequest dto ) {
        User user = this.obtenerUser (id);
        user.setNombre (dto.nombre ());
        user.setApellidos (dto.apellidos());
        user.setEmail(dto.email());
        user.setPassword (dto.password());
        user.setFecha_nacimiento (dto.fechaNacimiento());
        user.setFecha_comienzo_trabajo(dto.fechaInicioTrabajo());
        userRepository.actualizar(user);
    }
    
    @Override
    public User obtenerUser(String user) {
        return userRepository.obtener(user);
    }
}
