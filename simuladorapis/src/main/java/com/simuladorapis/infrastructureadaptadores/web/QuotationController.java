package com.simuladorapis.infrastructureadaptadores.web;

import com.simuladorapis.domain.usecase.GestionarCotizacionesUseCase;
import com.simuladorapis.domain.model.vo.QuotationId;
import com.simuladorapis.domain.model.vo.UserId;
import com.simuladorapis.infrastructureadaptadores.web.dto.UpdateQuotationRequest;
import com.simuladorapis.infrastructureadaptadores.web.dto.CreateQuotationRequest;
import com.simuladorapis.infrastructureadaptadores.web.dto.QuotationResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/{userId}/quotations")
public class QuotationController {

    private final GestionarCotizacionesUseCase useCase;

    @Autowired
    public QuotationController(GestionarCotizacionesUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public QuotationResponse crear(
            @PathVariable Long userId,
            @RequestBody CreateQuotationRequest request) {

        var quotation = useCase.crearCotizacion(
                new UserId(userId),
                request.amount(),
                request.date()
        );

        return QuotationResponse.fromDomain(quotation);
    }

    @GetMapping
    public List<QuotationResponse> listar(@PathVariable Long userId) {
        return useCase.obtenerCotizaciones(new UserId(userId))
                .stream()
                .map(QuotationResponse::fromDomain)
                .toList();
    }

    @DeleteMapping("/{quotationId}")
    public void borrar(
            @PathVariable Long userId,
            @PathVariable Long quotationId) {

        useCase.borrarCotizacion(new UserId(userId), new QuotationId(quotationId));
    }

    @DeleteMapping
    public void borrarTodas(@PathVariable Long userId) {
        useCase.borrarTodas(new UserId(userId));
    }

    @PutMapping("/{quotationId}")
    public QuotationResponse modificar(
            @PathVariable Long userId,
            @PathVariable Long quotationId,
            @RequestBody UpdateQuotationRequest request) {

        var quotation = useCase.modificarCotizacion(
                new UserId(userId),
                new QuotationId(quotationId),
                request.amount(),
                request.date()
        );

        return QuotationResponse.fromDomain(quotation);
    }
}
