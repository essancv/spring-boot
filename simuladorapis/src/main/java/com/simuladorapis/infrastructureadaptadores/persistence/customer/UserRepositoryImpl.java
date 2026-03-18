package com.simuladorapis.infrastructureadaptadores.persistence.customer;

import com.simuladorapis.domain.ports.UserRepository;
import com.simuladorapis.domain.model.User;

import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.HashMap;

import org.springframework.context.annotation.Profile;

import java.util.Optional;

@Repository
@Profile ("customer")
public class UserRepositoryImpl implements UserRepository {
    
    private final Map<String, User> users = new HashMap<>();
    
    @Override
    public void guardar(User user) {
        users.put(user.getUsername(), user);
    }
    
    @Override
    public void eliminar(String user) {
        users.remove(user);
    }
    
    @Override
    public void actualizar(User user) {
        users.put(user.getUsername(), user);
    }
    
    @Override
    public User obtener(String user) {
        return users.get(user);
    }
    @Override
    public Optional<User>  findByUsername (String username) {
        return Optional.ofNullable(users.get(username));
    }

}