package com.ecommerce.catalog.infrastructure.mapper;

import com.ecommerce.catalog.domain.model.Producto;
import com.ecommerce.catalog.infrastructure.driver_adapters.base_datos.ProductoData;
import com.ecommerce.catalog.infrastructure.entry_points.dto.ActualizarProductoRequestDTO;
import com.ecommerce.catalog.infrastructure.entry_points.dto.CrearProductoRequestDTO;
import com.ecommerce.catalog.infrastructure.entry_points.dto.ProductoDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto toProducto(ProductoData data) {
        if (data == null) {
            return null;
        }
        return Producto.builder()
                .idProducto(data.getIdProducto())
                .nombre(data.getNombre())
                .descripcion(data.getDescripcion())
                .precio(data.getPrecio())
                .stock(data.getStock())
                .fechaCreacion(data.getFechaCreacion())
                .build();
    }

    public ProductoData toProductoData(Producto domain) {
        if (domain == null) {
            return null;
        }
        return ProductoData.builder()
                .idProducto(domain.getIdProducto())
                .nombre(domain.getNombre())
                .descripcion(domain.getDescripcion())
                .precio(domain.getPrecio())
                .stock(domain.getStock())
                .fechaCreacion(domain.getFechaCreacion())
                .build();
    }

    public Producto dtoCrearToProducto(CrearProductoRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Producto.builder()
                .idProducto(null)
                .nombre(dto.nombre())
                .descripcion(dto.descripcion())
                .precio(dto.precio())
                .stock(dto.stock())
                .fechaCreacion(null)
                .build();
    }

    public Producto dtoActualizarToProducto(ActualizarProductoRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Producto.builder()
                .idProducto(dto.idProducto())
                .nombre(dto.nombre())
                .descripcion(dto.descripcion())
                .precio(dto.precio())
                .stock(dto.stock())
                .fechaCreacion(null)
                .build();
    }

    public ProductoDTO productoToDto(Producto domain) {
        if (domain == null) {
            return null;
        }
        return new ProductoDTO(
                domain.getIdProducto(),
                domain.getNombre(),
                domain.getDescripcion(),
                domain.getPrecio(),
                domain.getStock(),
                domain.getFechaCreacion()
        );
    }
}
