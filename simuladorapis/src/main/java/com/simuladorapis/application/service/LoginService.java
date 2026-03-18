package com.simuladorapis.application.service;

import com.simuladorapis.domain.model.User;
import com.simuladorapis.domain.ports.UserRepository;
import com.simuladorapis.domain.usecase.LoginUseCase;
import com.simuladorapis.application.usecases.results.LoginResult;
import com.simuladorapis.infrastructureadaptadores.web.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;

public class LoginService implements LoginUseCase {

    private final UserRepository  userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(UserRepository userRepositoryPort,
                        PasswordEncoder passwordEncoder,
                        JwtTokenProvider jwtTokenProvider) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public LoginResult  login(String username, String rawPassword) {
        User user = userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Invalid credentials"));

        if (!user.getActivo()) {
            throw new IllegalStateException("User is not active");
        }

        System.out.println ("Rawpassword = " + rawPassword);
        System.out.println ("Rawpassword encoded = " + passwordEncoder.encode(rawPassword));
        System.out.println ("BD Password = " + user.getPassword());
        
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            System.out.println ("No coinciden las credenciales");
            throw new NoSuchElementException("Invalid credentials");
        }
       
        String token = jwtTokenProvider.generateToken(user);

        return new LoginResult(user, token);    }
}
