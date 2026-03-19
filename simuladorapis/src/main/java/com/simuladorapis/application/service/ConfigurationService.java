package com.simuladorapis.application.service;

import com.simuladorapis.domain.usecase.ConfigurationUseCase;
import com.simuladorapis.domain.ports.ParametrosSimulacionRepository;
import com.simuladorapis.domain.model.ParametrosSimulacion;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.lang.Override;



@Service
public class ConfigurationService implements ConfigurationUseCase {

    private final ParametrosSimulacionRepository repository;

    @Autowired
    public ConfigurationService (ParametrosSimulacionRepository repository) {
        this.repository = repository;
    }

    @Override
    public ParametrosSimulacion leerConfiguration() {
        return repository.leeParametrosSimulacion();
    }
}