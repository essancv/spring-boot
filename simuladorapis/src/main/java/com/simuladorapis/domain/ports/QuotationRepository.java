package com.simuladorapis.domain.ports;

import com.simuladorapis.domain.model.Quotation;
import com.simuladorapis.domain.model.vo.UserId;
import com.simuladorapis.domain.model.vo.QuotationId;

import java.util.List;
import java.util.Optional;

public interface QuotationRepository {

    Quotation save(UserId userId, Quotation quotation);

    Optional<Quotation> findById(UserId userId, QuotationId quotationId);

    List<Quotation> findAllByUser(UserId userId);

    void delete(UserId userId, QuotationId quotationId);

    void deleteAll(UserId userId);
}
