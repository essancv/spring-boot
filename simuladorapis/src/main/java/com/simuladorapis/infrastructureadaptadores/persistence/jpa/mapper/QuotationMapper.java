package com.simuladorapis.infrastructureadaptadores.persistence.jpa.mapper;

import com.simuladorapis.domain.model.Quotation;
import com.simuladorapis.domain.model.vo.QuotationId;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.entities.QuotationEntity;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.entities.UserEntity;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.entities.SimulationEntity;


public class QuotationMapper {

    public static Quotation toDomain(QuotationEntity entity) {
        return new Quotation(
                entity.getId() != null ? new QuotationId(entity.getId()) : null,
                entity.getAmount(),
                entity.getDate()
        );
    }

    public static void updateEntity(QuotationEntity entity, Quotation quotation) {

    // Si el dominio trae ID, lo respetamos (normalmente ya coincide)
    if (quotation.getId() != null) {
        entity.setId(quotation.getId().value());
    }

    // Actualizamos los campos mutables
    entity.setAmount(quotation.getAmount());
    entity.setDate(quotation.getDate());

    }

    public static QuotationEntity toEntityForUser(Quotation quotation, UserEntity user) {
        QuotationEntity entity = new QuotationEntity();

        if (quotation.getId() != null)
            entity.setId(quotation.getId().value());

        entity.setAmount(quotation.getAmount());
        entity.setDate(quotation.getDate());
        entity.setUser(user);
        entity.setSimulation(null);

        return entity;
    }

    public static QuotationEntity toEntityForSimulation(Quotation quotation, SimulationEntity sim) {
        QuotationEntity entity = new QuotationEntity();

        if (quotation.getId() != null)
            entity.setId(quotation.getId().value());

        entity.setAmount(quotation.getAmount());
        entity.setDate(quotation.getDate());
        entity.setSimulation(sim);
        entity.setUser(null);

        return entity;
    }
}
