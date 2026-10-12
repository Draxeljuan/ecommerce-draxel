package com.ecommerce.catalog.infrastructure.driver_adapters.base_datos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "carritos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarritoData {

    @Id
    @Column(name = "id_carrito")
    private String idCarrito;

    @Column(name = "id_usuario", nullable = false)
    private String idUsuario;

    @Column(name = "estado", length = 50)
    private String estado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
}
