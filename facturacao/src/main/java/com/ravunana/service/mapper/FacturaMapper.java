package com.ravunana.service.mapper;

import com.ravunana.domain.*;
import com.ravunana.service.dto.FacturaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Factura} and its DTO {@link FacturaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FacturaMapper extends EntityMapper<FacturaDTO, Factura> {



    default Factura fromId(Long id) {
        if (id == null) {
            return null;
        }
        Factura factura = new Factura();
        factura.setId(id);
        return factura;
    }
}
