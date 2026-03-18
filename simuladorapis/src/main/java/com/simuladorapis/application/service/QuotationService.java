package com.simuladorapis.application.service;

import com.simuladorapis.domain.model.Quotation;
import com.simuladorapis.domain.model.vo.QuotationId;
import com.simuladorapis.domain.model.vo.UserId;
import com.simuladorapis.domain.ports.QuotationRepository;
import com.simuladorapis.domain.usecase.GestionarCotizacionesUseCase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.lang.Override;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuotationService implements GestionarCotizacionesUseCase {

    private final QuotationRepository quotationRepository;

    @Autowired
    public QuotationService (QuotationRepository quotationRepository) {
        this.quotationRepository = quotationRepository;
    }

    @Override
    public Quotation crearCotizacion(UserId userId, BigDecimal amount, LocalDate date) {
        Quotation quotation = new Quotation(amount, date);
        return quotationRepository.save(userId, quotation);
    }

    @Override
    public List<Quotation> obtenerCotizaciones(UserId userId) {
        return quotationRepository.findAllByUser(userId);
    }

    @Override
    public void borrarCotizacion(UserId userId, QuotationId quotationId) {
        quotationRepository.delete(userId, quotationId);
    }

    @Override
    public void borrarTodas(UserId userId) {
        quotationRepository.deleteAll(userId);
    }

    @Override
    public Quotation modificarCotizacion(UserId userId, QuotationId quotationId, BigDecimal amount, LocalDate date) {
        Quotation quotation = quotationRepository.findById(userId, quotationId)
                .orElseThrow(() -> new IllegalArgumentException("Quotation not found"));

        quotation.update(amount, date);

        return quotationRepository.save(userId, quotation);
    }
}
