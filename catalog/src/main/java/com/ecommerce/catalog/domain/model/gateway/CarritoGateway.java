package com.ecommerce.catalog.domain.model.gateway;

import com.ecommerce.catalog.domain.model.Carrito;
import com.ecommerce.catalog.domain.model.ItemCarrito;

import java.util.List;

public interface CarritoGateway {
    Carrito guardarCarrito(Carrito carrito);
    Carrito buscarPorId(String idCarrito);
    Carrito buscarActivoPorUsuario(String idUsuario);
    void guardarItem(ItemCarrito item);
    void eliminarItem(String idItem);
    List<ItemCarrito> buscarItemsPorCarrito(String idCarrito);
    ItemCarrito buscarItemPorId(String idItem);
    ItemCarrito buscarItemPorCarritoYProducto(String idCarrito, String idProducto);



}
