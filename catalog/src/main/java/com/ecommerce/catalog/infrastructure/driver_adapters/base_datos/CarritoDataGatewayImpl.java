package com.ecommerce.catalog.infrastructure.driver_adapters.base_datos;

import com.ecommerce.catalog.domain.model.Carrito;
import com.ecommerce.catalog.domain.model.ItemCarrito;
import com.ecommerce.catalog.domain.model.gateway.CarritoGateway;
import com.ecommerce.catalog.infrastructure.mapper.CarritoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CarritoDataGatewayImpl implements CarritoGateway {

    private final CarritoDataJpaRepository carritoDataJpaRepository;
    private final ItemCarritoDataJpaRepository itemCarritoDataJpaRepository;
    private final CarritoMapper carritoMapper;

    @Override
    public Carrito guardarCarrito(Carrito carrito) {
        CarritoData data = carritoMapper.toCarritoData(carrito);
        return carritoMapper.toCarrito(carritoDataJpaRepository.save(data));
    }

    @Override
    public Carrito buscarPorId(String idCarrito) {
        return carritoDataJpaRepository.findById(idCarrito)
                .map(carritoMapper::toCarrito)
                .orElse(null);
    }

    @Override
    public Carrito buscarActivoPorUsuario(String idUsuario) {
        return carritoDataJpaRepository.findByIdUsuarioAndEstado(idUsuario, "ACTIVO")
                .map(carritoMapper::toCarrito)
                .orElse(null);
    }

    @Override
    public void guardarItem(ItemCarrito item) {
        ItemCarritoData data = carritoMapper.toItemCarritoData(item);
        itemCarritoDataJpaRepository.save(data);
    }

    @Override
    public void eliminarItem(String idItem) {
        itemCarritoDataJpaRepository.deleteById(idItem);
    }

    @Override
    public List<ItemCarrito> buscarItemsPorCarrito(String idCarrito) {
        return itemCarritoDataJpaRepository.findByIdCarrito(idCarrito).stream()
                .map(carritoMapper::toItemCarrito)
                .toList();
    }

    @Override
    public ItemCarrito buscarItemPorId(String idItem) {
        return itemCarritoDataJpaRepository.findById(idItem)
                .map(carritoMapper::toItemCarrito)
                .orElse(null);
    }

    @Override
    public ItemCarrito buscarItemPorCarritoYProducto(String idCarrito, String idProducto) {
        return itemCarritoDataJpaRepository.findByIdCarritoAndIdProducto(idCarrito, idProducto)
                .map(carritoMapper::toItemCarrito)
                .orElse(null);
    }
}
