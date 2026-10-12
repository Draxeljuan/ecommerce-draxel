package com.ecommerce.catalog.domain.usecase;

import com.ecommerce.catalog.domain.exception.RecursoNoEncontradoException;
import com.ecommerce.catalog.domain.exception.ReglaNegocioException;
import com.ecommerce.catalog.domain.model.Producto;
import com.ecommerce.catalog.domain.model.gateway.ProductoGateway;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class ProductoUseCase {

    private final ProductoGateway productoGateway;

    public Producto guardarProducto(Producto producto) {
        if (producto == null) {
            throw new ReglaNegocioException("El producto no puede ser nulo");
        }

        validarDatosProducto(producto);
        validarNombreUnico(producto.getNombre(),null);

        producto.setIdProducto(generarIdProducto());
        producto.setFechaCreacion(LocalDateTime.now(ZoneId.systemDefault()));

        return productoGateway.guardarProducto(producto);
    }

    public Producto buscarPorId(String idProducto) {
        if (idProducto == null || idProducto.trim().isEmpty()) {
            throw new ReglaNegocioException("El ID del producto no puede ser nulo o vacío");
        }

        Producto producto = productoGateway.buscarPorId(idProducto);
        if (producto == null) {
            throw new RecursoNoEncontradoException("Producto no encontrado con ID: " + idProducto);
        }
        return producto;
    }

    public List<Producto> listarProductos() {
        return productoGateway.listarProductos();
    }

    public Producto actualizarProducto(Producto producto) {
        if (producto == null) {
            throw new ReglaNegocioException("El producto no puede ser nulo");
        }

        if (producto.getIdProducto() == null || producto.getIdProducto().trim().isEmpty()) {
            throw new ReglaNegocioException("Para actualizar un producto se requiere un ID válido");
        }

        Producto productoExistente = buscarPorId(producto.getIdProducto());

        validarDatosProducto(producto);

        validarNombreUnico(producto.getNombre(), productoExistente.getIdProducto());

        producto.setFechaCreacion(productoExistente.getFechaCreacion());

        return productoGateway.actualizarProducto(producto);
    }

    public void eliminarProducto(String idProducto) {
        if (idProducto == null || idProducto.trim().isEmpty()) {
            throw new ReglaNegocioException("Para eliminar un producto se requiere un ID válido");
        }

        buscarPorId(idProducto);

        if (productoGateway.estaAsociadoACarrito(idProducto)) {
            throw new ReglaNegocioException("No se puede eliminar el producto porque está presente en uno o más carritos de compra");
        }

        productoGateway.eliminarProducto(idProducto);
    }

    private void validarDatosProducto(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new ReglaNegocioException("El nombre del producto no puede estar vacío o nulo");
        }
        if (producto.getNombre().length() > 255) {
            throw new ReglaNegocioException("El nombre del producto excede el límite permitido de 255 caracteres");
        }
        if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("El precio del producto debe ser mayor a cero y no puede ser nulo");
        }
        if (producto.getPrecio().scale() > 2) {
            throw new ReglaNegocioException("El precio no puede tener más de 2 decimales");
        }
        if (producto.getStock() == null || producto.getStock() < 0) {
            throw new ReglaNegocioException("El stock del producto no puede ser negativo o nulo");
        }
    }

    private void validarNombreUnico(String nombre, String idProductoActual) {
        Producto productoConMismoNombre = productoGateway.buscarPorNombre(nombre.trim());

        if (productoConMismoNombre != null && !Objects.equals(productoConMismoNombre.getIdProducto(), idProductoActual)) {
            throw new ReglaNegocioException("Ya existe un producto registrado con el nombre: " + nombre);
        }
    }

    private String generarIdProducto() {
        return "PROD-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }
}
