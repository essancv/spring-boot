package com.simuladorapis.domain.usecase;

import com.simuladorapis.domain.model.Quotation;
import com.simuladorapis.domain.model.vo.QuotationId;
import com.simuladorapis.domain.model.vo.UserId;
import com.simuladorapis.domain.ports.QuotationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface GestionarCotizacionesUseCase {


    public Quotation crearCotizacion(UserId userId, BigDecimal amount, LocalDate date) ;
    public List<Quotation> obtenerCotizaciones(UserId userId) ;

    public void borrarCotizacion(UserId userId, QuotationId quotationId) ;

    public void borrarTodas(UserId userId);

    public Quotation modificarCotizacion(UserId userId, QuotationId quotationId, BigDecimal amount, LocalDate date);
}