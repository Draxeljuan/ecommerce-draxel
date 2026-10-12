package com.ecommerce.catalog.domain.model.gateway;

import com.ecommerce.catalog.domain.model.Producto;

import java.util.List;

public interface ProductoGateway {
    Producto guardarProducto(Producto producto);
    Producto buscarPorId(String idProducto);
    Producto buscarPorNombre(String nombreProducto);
    List<Producto> listarProductos();
    Producto actualizarProducto(Producto producto);
    void eliminarProducto(String idProducto);
    boolean estaAsociadoACarrito(String idProducto);
}
