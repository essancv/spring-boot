
package com.simuladorapis.infrastructureadaptadores.persistence.jpa.mapper;

import com.simuladorapis.domain.model.Simulation;
import com.simuladorapis.domain.model.vo.SimulationId;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.mapper.QuotationMapper;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.entities.SimulationEntity;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.entities.UserEntity;



public class SimulationMapper {

    public static Simulation toDomain(SimulationEntity entity) {
        return new Simulation(
                new SimulationId(entity.getId()),
                entity.getName(),
                entity.getCreatedAt(),
                entity.getQuotations().stream()
                        .map(QuotationMapper::toDomain)
                        .toList()
        );
    }

    public static SimulationEntity toEntity(Simulation sim, UserEntity user) {
        SimulationEntity entity = new SimulationEntity();

        if (sim.id() != null)
            entity.setId(sim.id().value());

        entity.setName(sim.name());
        entity.setCreatedAt(sim.createdAt());
        entity.setUser(user);

        var quotationEntities = sim.quotations().stream()
                .map(q -> QuotationMapper.toEntityForSimulation(q, entity))
                .toList();

        entity.setQuotations(quotationEntities);

        return entity;
    }
}
