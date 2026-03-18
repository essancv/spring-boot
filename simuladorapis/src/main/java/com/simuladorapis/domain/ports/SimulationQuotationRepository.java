package com.simuladorapis.domain.ports;

import com.simuladorapis.domain.model.vo.UserId;
import com.simuladorapis.domain.model.vo.SimulationId;
import com.simuladorapis.domain.model.Quotation;

public interface SimulationQuotationRepository {
    Quotation save(UserId userId, SimulationId simulationId , Quotation quotation);  
}