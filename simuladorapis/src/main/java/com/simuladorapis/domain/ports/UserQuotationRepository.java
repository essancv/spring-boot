package com.simuladorapis.domain.ports;

import com.simuladorapis.domain.model.vo.UserId;
import com.simuladorapis.domain.model.vo.QuotationId;
import com.simuladorapis.domain.model.Quotation;

import java.util.List;

public interface UserQuotationRepository {
    Quotation save(UserId userId, Quotation quotation);  
    List<Quotation> findAll (UserId userid);  
    void delete (UserId userId , QuotationId quotationId);
}