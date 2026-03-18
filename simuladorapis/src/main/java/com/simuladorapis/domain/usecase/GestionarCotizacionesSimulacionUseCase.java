package com.simuladorapis.domain.usecase;

import com.simuladorapis.domain.model.Quotation;
import com.simuladorapis.domain.model.vo.QuotationId;
import com.simuladorapis.domain.model.vo.SimulationId;
import com.simuladorapis.domain.model.vo.UserId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface GestionarCotizacionesSimulacionUseCase {

    public Quotation crearCotizacion(UserId userId, SimulationId simulationId,
                                     BigDecimal amount, LocalDate date);

    public List<Quotation> obtenerCotizaciones(UserId userId, SimulationId simulationId) ;

    public Quotation modificarCotizacion(UserId userId, SimulationId simulationId,
                                         QuotationId quotationId,
                                         BigDecimal amount, LocalDate date);

    public void borrarCotizacion(UserId userId, SimulationId simulationId, QuotationId quotationId);
    
    public void borrarTodas(UserId userId, SimulationId simulationId) ;
}
