package com.simuladorapis.infrastructureadaptadores.persistence.jpa;

import com.simuladorapis.domain.model.User;
import com.simuladorapis.domain.model.Quotation;
import com.simuladorapis.domain.model.vo.QuotationId;
import com.simuladorapis.domain.ports.UserRepository;
import org.springframework.stereotype.Repository;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.entities.UserEntity;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.entities.QuotationEntity;
import com.simuladorapis.domain.model.vo.UserId;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.interfaces.UserQuotationJpaRepository;
import org.springframework.context.annotation.Profile;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.mapper.UserMapper;
import com.simuladorapis.infrastructureadaptadores.persistence.jpa.mapper.QuotationMapper;
import com.simuladorapis.domain.ports.UserQuotationRepository;

import java.util.List;

@Repository
@Profile("jpa")
public class UserQuotationRepositoryImpl implements UserQuotationRepository {

    private final UserQuotationJpaRepository userRepo;

    public UserQuotationRepositoryImpl(UserQuotationJpaRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Quotation save(UserId userId, Quotation quotation) {

        UserEntity user = userRepo.findById(userId.value())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        QuotationEntity entity = QuotationMapper.toEntityForUser(quotation, user);

        user.getQuotations().add(entity);

        UserEntity saved = userRepo.save(user);

        QuotationEntity persisted = saved.getQuotations()
                .stream()
                .filter(q -> q.getAmount().equals(quotation.getAmount())
                          && q.getDate().equals(quotation.getDate()))
                .reduce((a, b) -> b)
                .orElseThrow();

        return QuotationMapper.toDomain(persisted);
    }

    public List<Quotation> findAll(UserId userId) {
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
}
