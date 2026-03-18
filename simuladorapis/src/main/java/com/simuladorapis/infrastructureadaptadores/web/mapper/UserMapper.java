package com.simuladorapis.infrastructureadaptadores.web.mapper;

import com.simuladorapis.domain.model.User;
import com.simuladorapis.domain.model.vo.UserId;
import com.simuladorapis.infrastructureadaptadores.web.dto.UpdateUserRequest;
import com.simuladorapis.infrastructureadaptadores.web.dto.UserResponseDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



public class UserMapper {


    public static UserResponseDto toDto(User user) {
        System.out.println  ("Estamos en UserMapper::toDto " + user.getId ());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

        String fechaNacimiento = "";
        String fechaInicioTrabajo = "";
        if (user.getFecha_nacimiento() != null ) {
            LocalDateTime  fecha = LocalDateTime.parse(user.getFecha_nacimiento().toString () , formatter);
            fechaNacimiento = fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        if (user.getFecha_comienzo_trabajo() != null ) {
            LocalDateTime  fecha = LocalDateTime.parse(user.getFecha_comienzo_trabajo().toString () , formatter);
           fechaInicioTrabajo = fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        
        return new UserResponseDto ( 
            user.getId ().value ().toString(),
            user.getUsername(),
            user.getNombre(),
            user.getApellidos(),
            user.getEmail(),
            user.getPassword() ,
            fechaNacimiento,
            fechaInicioTrabajo,
            user.getQuotations(),
            user.getSimulations()
        );
    }

}
