package com.simuladorapis.infrastructureadaptadores.web;

import com.simuladorapis.domain.usecase.GestionarUsersUseCase;
import com.simuladorapis.domain.model.User;
import com.simuladorapis.domain.model.vo.UserId;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import com.simuladorapis.infrastructureadaptadores.web.dto.CreateUserRequest;
import com.simuladorapis.infrastructureadaptadores.web.dto.UpdateUserRequest;
import com.simuladorapis.infrastructureadaptadores.web.dto.UserResponseDto;
import com.simuladorapis.infrastructureadaptadores.web.mapper.UserMapper;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    private final GestionarUsersUseCase gestionarUsersUseCase;
    
    @Autowired
    public UserController(GestionarUsersUseCase gestionarUsersUseCase) {
        this.gestionarUsersUseCase = gestionarUsersUseCase;
    }
    
    @PostMapping
    public void darDeAltaUser(@RequestBody CreateUserRequest request) {
        gestionarUsersUseCase.darDeAltaUser(request.username () , request.nombre() , request.apellidos(),request.email(),request.password () , request.fechaNacimiento(),request.fechaInicioTrabajo());
    }
    
    @DeleteMapping("/{user}")
    public void darDeBajaUser(@PathVariable String user) {
        gestionarUsersUseCase.darDeBajaUser(user);
    }
    
    @PutMapping("/{id}")
    public void modificarUser(@PathVariable String id, @RequestBody UpdateUserRequest userActualizado) {
        System.out.println (" *****************   Estamos en usercontroller " +  id);
        gestionarUsersUseCase.modificarUser( id ,userActualizado );
    }
    
    @GetMapping("/{id}")
    public UserResponseDto obtenerUser(@PathVariable String id) {
        User user = gestionarUsersUseCase.obtenerUser(id);
        return UserMapper.toDto (user);
//        return gestionarUsersUseCase.obtenerUser(user);
    }
}
