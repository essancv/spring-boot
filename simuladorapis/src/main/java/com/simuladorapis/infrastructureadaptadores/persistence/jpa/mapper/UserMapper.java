package com.simuladorapis.infrastructureadaptadores.persistence.jpa.mapper;


import com.simuladorapis.domain.model.User;
import com.simuladorapis.domain.model.vo.UserId;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.mapper.SimulationMapper;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.mapper.QuotationMapper;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.entities.UserEntity;

import java.util.stream.Collectors;

public class UserMapper {

    public static User toDomain(UserEntity entity) {
        if (entity == null) return null;
        
        return new User(
                new UserId(entity.getId()),
                entity.getUsername(),
                entity.getNombre(),
                entity.getApellidos(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getFecha_nacimiento(),
                entity.getFecha_comienzo_trabajo(),
                entity.getActivo (),
                entity.getQuotations()
                        .stream()
                        .map(QuotationMapper::toDomain)
                        .collect(Collectors.toList()),
                entity.getSimulations()
                        .stream()
                        .map(SimulationMapper::toDomain)
                        .collect(Collectors.toList())
        );

    }

    public static UserEntity toEntity(User user) {
        if (user == null) return null;
        System.out.println (" ************* estamos en UserMapper toEntity ");

        UserEntity entity = new UserEntity();

        if (user.getId() != null) {
            System.out.println (" *************  ID " + user.getId());
            entity.setId(user.getId().value());
        }

        entity.setUsername(user.getUsername());
        entity.setNombre(user.getNombre());
        entity.setApellidos(user.getApellidos());
        entity.setEmail(user.getEmail());
        entity.setActivo (user.getActivo ());
        entity.setPassword(user.getPassword());
        entity.setFecha_nacimiento (user.getFecha_nacimiento());
        entity.setFecha_comienzo_trabajo (user.getFecha_comienzo_trabajo());

        // Relación OneToMany
        entity.setQuotations(
                user.getQuotations()
                        .stream()
                        .map(q -> QuotationMapper.toEntityForUser(q, entity))
                        .collect(Collectors.toList())
        );

        return entity;
    }
}
