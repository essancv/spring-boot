package com.simuladorapis.infrastructureadaptadores.persistence;

import com.simuladorapis.domain.model.Quotation;
import com.simuladorapis.domain.model.vo.QuotationId;
import com.simuladorapis.domain.model.vo.UserId;
import com.simuladorapis.domain.ports.QuotationRepository;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.entities.UserEntity;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.entities.QuotationEntity;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.interfaces.QuotationJpaRepository;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.mapper.QuotationMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class QuotationRepositoryImpl implements QuotationRepository {

    private final QuotationJpaRepository userRepo;

    public QuotationRepositoryImpl(QuotationJpaRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Quotation save(UserId userId, Quotation quotation) {

        UserEntity user = userRepo.findById(userId.value())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (quotation.getId() == null) {
                // es una creación

                // Creamos la entidad sin ID
                QuotationEntity entity = QuotationMapper.toEntityForUser(quotation, user);

                // La añadimos al usuario
                user.getQuotations().add(entity);

                // Guardamos el usuario (esto genera el ID de la cotización)
                UserEntity savedUser = userRepo.save(user);

                // Recuperamos la cotización persistida (la que sí tiene ID)
                QuotationEntity persisted = savedUser.getQuotations()
                        .stream()
                        .filter(q -> q.getAmount().equals(quotation.getAmount())
                                && q.getDate().equals(quotation.getDate()))
                        .reduce((first, second) -> second) // por si hay varias iguales
                        .orElseThrow(() -> new IllegalStateException("Quotation not persisted"));

                return QuotationMapper.toDomain(persisted);
        }  else {
                // Buscar si ya existe
                System.out.println ("QuotationRepositoryImpl - ver si existe previamente la cotización : ");
                var existing = user.getQuotations().stream()
                        .filter(q -> q.getId().equals(quotation.getId().value()))
                        .findFirst();

                System.out.println ("QuotationRepositoryImpl -existe : " + existing.isPresent());

                if (existing.isPresent()) {
                        // actualizar la entidad existente
                        QuotationMapper.updateEntity(existing.get(), quotation);
                        userRepo.save(user); 
                        return QuotationMapper.toDomain(existing.get());
                } 
                
        }
        throw new IllegalArgumentException("Quotation not found for user");
    }


    public Optional<Quotation> findById(UserId userId, QuotationId quotationId) {
        return userRepo.findById(userId.value())
                .flatMap(u -> u.getQuotations().stream()
                        .filter(q -> q.getId().equals(quotationId.value()))
                        .findFirst()
                        .map(QuotationMapper::toDomain));
    }

    public List<Quotation> findAllByUser(UserId userId) {
        return userRepo.findById(userId.value())
                .orElseThrow()
                .getQuotations()
                .stream()
                .map(QuotationMapper::toDomain)
                .toList();
    }

    public void delete(UserId userId, QuotationId quotationId) {
        UserEntity user = userRepo.findById(userId.value())
                .orElseThrow();

        user.getQuotations().removeIf(q -> q.getId().equals(quotationId.value()));

        userRepo.save(user);
    }

    public void deleteAll(UserId userId) {
        UserEntity user = userRepo.findById(userId.value())
                .orElseThrow();

        user.getQuotations().clear();

        userRepo.save(user);
    }
}
