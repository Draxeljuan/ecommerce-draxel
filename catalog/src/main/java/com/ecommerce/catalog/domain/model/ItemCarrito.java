package com.ecommerce.catalog.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCarrito {
    private String idItem;
    private String idCarrito;
    private String idProducto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
}
