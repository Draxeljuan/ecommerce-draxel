package com.ecommerce.catalog.infrastructure.driver_adapters.base_datos;

import com.ecommerce.catalog.domain.model.Producto;
import com.ecommerce.catalog.domain.model.gateway.ProductoGateway;
import com.ecommerce.catalog.infrastructure.mapper.ProductoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductoDataGatewayImpl implements ProductoGateway {

    private final ProductoDataJpaRepository productoDataJpaRepository;
    private final ItemCarritoDataJpaRepository itemCarritoDataJpaRepository;
    private final ProductoMapper productoMapper;

    @Override
    public Producto guardarProducto(Producto producto) {
        ProductoData data = productoMapper.toProductoData(producto);
        return productoMapper.toProducto(productoDataJpaRepository.save(data));
    }

    @Override
    public Producto buscarPorId(String idProducto) {
        return productoDataJpaRepository.findById(idProducto)
                .map(productoMapper::toProducto)
                .orElse(null);
    }

    @Override
    public Producto buscarPorNombre(String nombreProducto){
        return productoDataJpaRepository.findByNombreIgnoreCase(nombreProducto)
                .map(productoMapper::toProducto)
                .orElse(null);
    }

    @Override
    public List<Producto> listarProductos() {
        return productoDataJpaRepository.findAll().stream()
                .map(productoMapper::toProducto)
                .toList();
    }

    @Override
    public Producto actualizarProducto(Producto producto) {
        ProductoData data = productoMapper.toProductoData(producto);
        return productoMapper.toProducto(productoDataJpaRepository.save(data));
    }

    @Override
    public void eliminarProducto(String idProducto) {
        productoDataJpaRepository.deleteById(idProducto);
    }

    @Override
    public boolean estaAsociadoACarrito(String idProducto) {
        return itemCarritoDataJpaRepository.existsByIdProducto(idProducto);
    }
}
