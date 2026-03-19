package com.simuladorapis.infrastructureadaptadores.config;

import com.simuladorapis.application.service.LoginService;
import com.simuladorapis.domain.ports.UserRepository;
import com.simuladorapis.domain.usecase.LoginUseCase;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.UserRepositoryJPAImpl;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.interfaces.UserJpaRepository;
import com.simuladorapis.infrastructureadaptadores.web.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Profile;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationConfig   implements WebMvcConfigurer {

    @Bean
    @Profile("jpa")
    public UserRepository userRepositoryPort(UserJpaRepository userJpaRepository) {
        return new UserRepositoryJPAImpl(userJpaRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-minutes}") long expirationMinutes
    ) {
        return new JwtTokenProvider(secret, expirationMinutes);
    }

    @Bean
    public LoginUseCase loginUseCase(UserRepository userRepositoryPort,
                                     PasswordEncoder passwordEncoder,
                                     JwtTokenProvider jwtTokenProvider) {
        return new LoginService(userRepositoryPort, passwordEncoder, jwtTokenProvider);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/index.html")
                .addResourceLocations("classpath:/static/index.html")
                .setCachePeriod(0);
    }
    
}
