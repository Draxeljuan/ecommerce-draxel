package com.ecommerce.catalog.infrastructure.driver_adapters.base_datos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "items_carrito")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCarritoData {

    @Id
    @Column(name = "id_item")
    private String idItem;

    @Column(name = "id_carrito", nullable = false)
    private String idCarrito;

    @Column(name = "id_producto", nullable = false)
    private String idProducto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;
}
