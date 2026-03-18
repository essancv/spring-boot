package com.simuladorapis.infrastructureadaptadores.persistence.jpa;

import com.simuladorapis.domain.model.User;
import com.simuladorapis.domain.ports.UserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Profile;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.entities.UserEntity;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.interfaces.UserJpaRepository;
import com.simuladorapis.domain.model.vo.UserId;

import com.simuladorapis.infrastructureadaptadores.persistence.jpa.mapper.UserMapper;

import java.util.Optional;

// Eliminamos porque el bean ya está en ApplicationConfig
// @Repository
// @Profile("jpa")
public class UserRepositoryJPAImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    public UserRepositoryJPAImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public void guardar(User user) {
        UserEntity userEntity = toEntity(user);
        userJpaRepository.save(userEntity);
    }

    @Override
    public void eliminar(String username) {
        userJpaRepository.deleteById(username);
    }

    @Override
    public void actualizar(User user) {
        UserEntity userEntity = toEntity(user);
        userJpaRepository.save(userEntity); // JPA save actualiza si el ID ya existe
    }

    @Override
    public User obtener(String id) {
        System.out.println ("Estamos en lectura de usuario " + id);
        return userJpaRepository.findById(id)
                .map(this::toModel)
                .orElse(null);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username)
                .map(UserMapper::toDomain);
    }

    // Mapeo entre User y UserEntity
    private UserEntity toEntity(User user) {
        return UserMapper.toEntity (user);
        /*
        UserEntity userEntity= new UserEntity ();
        userEntity.setId (new UserId (user.getId()) );
        userEntity.setUsername (user.getUsername());
        userEntity.setNombre (user.getNombre());
        userEntity.setApellidos (user.getApellidos());
        userEntity.setEmail (user.getEmail());
        userEntity.setActivo (user.getActivo());
        userEntity.setPassword (user.getPassword());
        userEntity.setQuotations(
            user.getQuotations().stream()
                .map(q -> QuotationMapper.toEntity(q, entity))
                .toList()
        );
  
        return userEntity;
      */
     
         }


    private User toModel(UserEntity userEntity) {
        System.out.println ("Mapeo entidad a modelo : " + userEntity.getNombre () );

        return UserMapper.toDomain (userEntity);
        /*
        User user= new User (new UserId (userEntity.getId ()) , 
                                        userEntity.getUsername(),
                                        userEntity.getNombre(),
                                        userEntity.getApellidos(),
                                        userEntity.getEmail(),
                                        userEntity.getPassword(),
                                        userEntity.getActivo());
        user.setQuotations(
            userEntity.getQuotations().stream()
                .map(QuotationMapper::toDomain)
                .toList()
        );

        return user;
        */
    }
}
