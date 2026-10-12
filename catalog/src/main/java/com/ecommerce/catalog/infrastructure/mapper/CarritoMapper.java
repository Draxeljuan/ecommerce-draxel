package com.ecommerce.catalog.infrastructure.mapper;

import com.ecommerce.catalog.domain.model.Carrito;
import com.ecommerce.catalog.domain.model.ItemCarrito;
import com.ecommerce.catalog.infrastructure.driver_adapters.base_datos.CarritoData;
import com.ecommerce.catalog.infrastructure.driver_adapters.base_datos.ItemCarritoData;
import com.ecommerce.catalog.infrastructure.entry_points.dto.CarritoDTO;
import com.ecommerce.catalog.infrastructure.entry_points.dto.CheckoutResponseDTO;
import com.ecommerce.catalog.infrastructure.entry_points.dto.ItemCarritoDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class CarritoMapper {

    public Carrito toCarrito(CarritoData data) {
        if (data == null) {
            return null;
        }
        return Carrito.builder()
                .idCarrito(data.getIdCarrito())
                .idUsuario(data.getIdUsuario())
                .estado(data.getEstado())
                .fechaCreacion(data.getFechaCreacion())
                .items(new ArrayList<>())
                .build();
    }

    public CarritoData toCarritoData(Carrito domain) {
        if (domain == null) {
            return null;
        }
        return CarritoData.builder()
                .idCarrito(domain.getIdCarrito())
                .idUsuario(domain.getIdUsuario())
                .estado(domain.getEstado())
                .fechaCreacion(domain.getFechaCreacion())
                .build();
    }

    public ItemCarrito toItemCarrito(ItemCarritoData data) {
        if (data == null) {
            return null;
        }
        return ItemCarrito.builder()
                .idItem(data.getIdItem())
                .idCarrito(data.getIdCarrito())
                .idProducto(data.getIdProducto())
                .cantidad(data.getCantidad())
                .precioUnitario(data.getPrecioUnitario())
                .build();
    }

    public ItemCarritoData toItemCarritoData(ItemCarrito domain) {
        if (domain == null) {
            return null;
        }
        return ItemCarritoData.builder()
                .idItem(domain.getIdItem())
                .idCarrito(domain.getIdCarrito())
                .idProducto(domain.getIdProducto())
                .cantidad(domain.getCantidad())
                .precioUnitario(domain.getPrecioUnitario())
                .build();
    }

    public ItemCarritoDTO itemCarritoToDto(ItemCarrito domain) {
        if (domain == null) {
            return null;
        }
        return new ItemCarritoDTO(
                domain.getIdItem(),
                domain.getIdCarrito(),
                domain.getIdProducto(),
                domain.getCantidad(),
                domain.getPrecioUnitario()
        );
    }

    public List<ItemCarritoDTO> itemsToDtoList(List<ItemCarrito> items) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }
        return items.stream()
                .map(this::itemCarritoToDto)
                .toList();
    }

    public CarritoDTO carritoToDto(Carrito domain) {
        if (domain == null) {
            return null;
        }
        return new CarritoDTO(
                domain.getIdCarrito(),
                domain.getIdUsuario(),
                domain.getEstado(),
                domain.getFechaCreacion(),
                itemsToDtoList(domain.getItems())
        );
    }

    public CheckoutResponseDTO toCheckoutResponseDto(Carrito carrito, String mensaje) {
        if (carrito == null) {
            return null;
        }

        return new CheckoutResponseDTO(
                carrito.getIdCarrito(),
                carrito.getIdUsuario(),
                carrito.getEstado(),
                carrito.calcularTotal(),
                itemsToDtoList(carrito.getItems()),
                mensaje
        );
    }

}
