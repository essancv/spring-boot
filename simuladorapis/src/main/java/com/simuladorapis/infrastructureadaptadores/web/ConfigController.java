package com.simuladorapis.infrastructureadaptadores.web;

import com.simuladorapis.domain.usecase.ConfigurationUseCase;
import com.simuladorapis.infrastructureadaptadores.web.dto.ParametrosConfigurationResponse;;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/config")
public class ConfigController {
    
    private final ConfigurationUseCase configurationUseCase;
    
    @Autowired
    public ConfigController(ConfigurationUseCase configurationUseCase) {
        this.configurationUseCase = configurationUseCase;
    }
    
    @GetMapping
    public ParametrosConfigurationResponse obtenerConfiguration() {
        var params = configurationUseCase.leerConfiguration();
        return ParametrosConfigurationResponse.from(params);
    }
}
