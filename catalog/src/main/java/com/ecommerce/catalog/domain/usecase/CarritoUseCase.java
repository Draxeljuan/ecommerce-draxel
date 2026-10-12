package com.ecommerce.catalog.domain.usecase;

import com.ecommerce.catalog.domain.exception.RecursoNoEncontradoException;
import com.ecommerce.catalog.domain.exception.ReglaNegocioException;
import com.ecommerce.catalog.domain.model.Carrito;
import com.ecommerce.catalog.domain.model.ItemCarrito;
import com.ecommerce.catalog.domain.model.Producto;
import com.ecommerce.catalog.domain.model.gateway.CarritoGateway;
import com.ecommerce.catalog.domain.model.gateway.ProductoGateway;
import com.ecommerce.catalog.domain.model.gateway.UsuarioAuthGateway;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class CarritoUseCase {

    private final CarritoGateway carritoGateway;
    private final ProductoGateway productoGateway;
    private final UsuarioAuthGateway usuarioAuthGateway;

    private static final String ACTIVO = "ACTIVO";

    public Carrito obtenerOCrearCarritoActivo(String idUsuario) {
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new ReglaNegocioException("El ID de usuario no puede ser nulo o vacío");
        }

        // Validación de si existe el usuario antes de buscarle un carrito
        if (!usuarioAuthGateway.existeUsuario(idUsuario)) {
            throw new RecursoNoEncontradoException("No se pudo encontrar el usuario con id: " + idUsuario);
        }

        Carrito carritoActivo = carritoGateway.buscarActivoPorUsuario(idUsuario);


        if (carritoActivo != null) {
            carritoActivo.setItems(carritoGateway.buscarItemsPorCarrito(carritoActivo.getIdCarrito()));
            return carritoActivo;
        }

        Carrito nuevoCarrito = Carrito.builder()
                .idCarrito(generarIdCarrito())
                .idUsuario(idUsuario)
                .estado(ACTIVO)
                .fechaCreacion(LocalDateTime.now(ZoneId.systemDefault()))
                .items(new ArrayList<>())
                .build();

        return carritoGateway.guardarCarrito(nuevoCarrito);
    }

    public Carrito buscarPorId(String idCarrito) {
        if (idCarrito == null || idCarrito.trim().isEmpty()) {
            throw new ReglaNegocioException("El ID del carrito no puede ser nulo o vacío");
        }

        Carrito carrito = carritoGateway.buscarPorId(idCarrito);
        if (carrito == null) {
            throw new RecursoNoEncontradoException("Carrito no encontrado con ID: " + idCarrito);
        }

        carrito.setItems(carritoGateway.buscarItemsPorCarrito(idCarrito));
        return carrito;
    }

    public Carrito agregarItem(String idUsuario, String idProducto, Integer cantidad) {
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new ReglaNegocioException("El ID de usuario es obligatorio");
        }
        if (idProducto == null || idProducto.trim().isEmpty()) {
            throw new ReglaNegocioException("El ID de producto es obligatorio");
        }
        if (cantidad == null || cantidad <= 0) {
            throw new ReglaNegocioException("La cantidad debe ser mayor a cero");
        }

        Producto producto = productoGateway.buscarPorId(idProducto);
        if (producto == null) {
            throw new RecursoNoEncontradoException("Producto no encontrado con ID: " + idProducto);
        }

        Carrito carrito = obtenerOCrearCarritoActivo(idUsuario);

        ItemCarrito itemExistente = carritoGateway.buscarItemPorCarritoYProducto(carrito.getIdCarrito(), idProducto);

        if (itemExistente != null) {
            int nuevaCantidad = itemExistente.getCantidad() + cantidad;
            if (nuevaCantidad > producto.getStock()) {
                throw new ReglaNegocioException("La cantidad total (" + nuevaCantidad + ") supera el stock disponible (" + producto.getStock() + ")");
            }
            itemExistente.setCantidad(nuevaCantidad);
            itemExistente.setPrecioUnitario(producto.getPrecio());
            carritoGateway.guardarItem(itemExistente);
        } else {
            if (cantidad > producto.getStock()) {
                throw new ReglaNegocioException("La cantidad solicitada (" + cantidad + ") supera el stock disponible (" + producto.getStock() + ")");
            }
            ItemCarrito nuevoItem = ItemCarrito.builder()
                    .idItem(generarIdItem())
                    .idCarrito(carrito.getIdCarrito())
                    .idProducto(idProducto)
                    .cantidad(cantidad)
                    .precioUnitario(producto.getPrecio())
                    .build();
            carritoGateway.guardarItem(nuevoItem);
        }

        return buscarPorId(carrito.getIdCarrito());
    }

    public void eliminarItem(String idCarrito, String idItem) {
        if (idCarrito == null || idCarrito.trim().isEmpty()) {
            throw new ReglaNegocioException("El ID del carrito es obligatorio");
        }
        if (idItem == null || idItem.trim().isEmpty()) {
            throw new ReglaNegocioException("El ID del ítem es obligatorio");
        }

        Carrito carrito = buscarPorId(idCarrito);
        if (!ACTIVO.equalsIgnoreCase(carrito.getEstado())) {
            throw new ReglaNegocioException("No se pueden eliminar ítems de un carrito que no esté ACTIVO");
        }

        ItemCarrito item = carritoGateway.buscarItemPorId(idItem);
        if (item == null || !idCarrito.equals(item.getIdCarrito())) {
            throw new RecursoNoEncontradoException("El ítem no pertenece al carrito especificado");
        }

        carritoGateway.eliminarItem(idItem);
    }

    public Carrito checkout(String idCarrito) {
        if (idCarrito == null || idCarrito.trim().isEmpty()) {
            throw new ReglaNegocioException("El ID del carrito es obligatorio");
        }

        Carrito carrito = buscarPorId(idCarrito);

        if (!ACTIVO.equalsIgnoreCase(carrito.getEstado())) {
            throw new ReglaNegocioException("Solo se puede realizar checkout en carritos con estado ACTIVO");
        }

        List<ItemCarrito> items = carrito.getItems();
        if (items == null || items.isEmpty()) {
            throw new ReglaNegocioException("El carrito no contiene ítems para realizar la compra");
        }

        BigDecimal total = BigDecimal.ZERO;
        List<Producto> productosActualizados = new ArrayList<>();

        // Validamos stock y calculamos total
        for (ItemCarrito item : items) {
            Producto producto = productoGateway.buscarPorId(item.getIdProducto());
            if (producto == null) {
                throw new RecursoNoEncontradoException("Producto no encontrado durante el checkout: " + item.getIdProducto());
            }

            if (item.getCantidad() > producto.getStock()) {
                throw new ReglaNegocioException("Stock insuficiente para el producto: " + producto.getNombre()
                        + ". Solicitado: " + item.getCantidad() + ", Disponible: " + producto.getStock());
            }

            BigDecimal subtotal = item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad()));
            total = total.add(subtotal);

            producto.setStock(producto.getStock() - item.getCantidad());
            productosActualizados.add(producto);
        }

        // Validamos el saldo del usuario
        BigDecimal saldoUsuario = usuarioAuthGateway.obtenerSaldoUsuario(carrito.getIdUsuario());
        if (saldoUsuario == null) {
            throw new ReglaNegocioException("No se pudo verificar el saldo del usuario");
        }

        if (saldoUsuario.compareTo(total) < 0) {
            throw new ReglaNegocioException("Saldo insuficiente. Saldo actual: " + saldoUsuario + ", Total compra: " + total);
        }

        // Actualización de Saldo (Cobro)
        usuarioAuthGateway.descontarSaldo(carrito.getIdUsuario(), total);

        // Actualizar Stock (Persistir cambios)
        for (Producto prod : productosActualizados) {
            productoGateway.actualizarProducto(prod);
        }

        // Guardar carrito como vendido
        carrito.setEstado("VENDIDO");
        Carrito carritoVendido = carritoGateway.guardarCarrito(carrito);
        carritoVendido.setItems(items);

        return carritoVendido;
    }

    private String generarIdCarrito() {
        return "CAR-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }

    private String generarIdItem() {
        return "ITEM-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }
}
