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
    private static final String VENDIDO = "VENDIDO";

    public Carrito obtenerCarritoActivoPorUsuario(String idUsuario) {
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new ReglaNegocioException("El ID de usuario no puede ser nulo o vacío");
        }

        String idUsuarioLimpio = idUsuario.trim();

        // Validación de si existe el usuario antes de buscarle un carrito
        if (!usuarioAuthGateway.existeUsuario(idUsuarioLimpio)) {
            throw new RecursoNoEncontradoException("No se pudo encontrar el usuario con id: " + idUsuarioLimpio);
        }

        Carrito carritoActivo = carritoGateway.buscarActivoPorUsuario(idUsuarioLimpio);
        if (carritoActivo == null) {
            throw new RecursoNoEncontradoException("El usuario con id " + idUsuarioLimpio + " no tiene un carrito activo");
        }

        carritoActivo.setItems(carritoGateway.buscarItemsPorCarrito(carritoActivo.getIdCarrito()));
        return carritoActivo;
    }

    public Carrito buscarPorId(String idCarrito) {
        if (idCarrito == null || idCarrito.trim().isEmpty()) {
            throw new ReglaNegocioException("El ID del carrito no puede ser nulo o vacío");
        }

        Carrito carrito = carritoGateway.buscarPorId(idCarrito.trim());
        if (carrito == null) {
            throw new RecursoNoEncontradoException("Carrito no encontrado con ID: " + idCarrito);
        }

        carrito.setItems(carritoGateway.buscarItemsPorCarrito(carrito.getIdCarrito()));
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

        String idUsuarioLimpio = idUsuario.trim();
        String idProductoLimpio = idProducto.trim();

        if (!usuarioAuthGateway.existeUsuario(idUsuarioLimpio)) {
            throw new RecursoNoEncontradoException("No se pudo encontrar el usuario con id: " + idUsuarioLimpio);
        }

        Producto producto = productoGateway.buscarPorId(idProductoLimpio);
        if (producto == null) {
            throw new RecursoNoEncontradoException("Producto no encontrado con ID: " + idProductoLimpio);
        }

        Carrito carrito = carritoGateway.buscarActivoPorUsuario(idUsuarioLimpio);
        if (carrito == null) {
            carrito = crearCarritoActivo(idUsuarioLimpio);
        }

        ItemCarrito itemExistente = carritoGateway.buscarItemPorCarritoYProducto(carrito.getIdCarrito(), idProductoLimpio);

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
                    .idProducto(idProductoLimpio)
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

        String idCarritoLimpio = idCarrito.trim();
        String idItemLimpio = idItem.trim();

        Carrito carrito = buscarPorId(idCarritoLimpio);
        if (!ACTIVO.equalsIgnoreCase(carrito.getEstado())) {
            throw new ReglaNegocioException("No se pueden eliminar ítems de un carrito que no esté ACTIVO");
        }

        ItemCarrito item = carritoGateway.buscarItemPorId(idItemLimpio);
        if (item == null || !idCarritoLimpio.equals(item.getIdCarrito())) {
            throw new RecursoNoEncontradoException("El ítem no pertenece al carrito especificado");
        }

        carritoGateway.eliminarItem(idItemLimpio);
    }

    public Carrito checkout(String idUsuario) {
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new ReglaNegocioException("El ID de usuario es obligatorio");
        }

        Carrito carrito = obtenerCarritoActivoPorUsuario(idUsuario.trim());

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
        carrito.setEstado(VENDIDO);
        Carrito carritoVendido = carritoGateway.guardarCarrito(carrito);
        carritoVendido.setItems(items);

        return carritoVendido;
    }

    private Carrito crearCarritoActivo(String idUsuario) {
        Carrito nuevoCarrito = Carrito.builder()
                .idCarrito(generarIdCarrito())
                .idUsuario(idUsuario)
                .estado(ACTIVO)
                .fechaCreacion(LocalDateTime.now(ZoneId.systemDefault()))
                .items(new ArrayList<>())
                .build();

        return carritoGateway.guardarCarrito(nuevoCarrito);
    }

    private String generarIdCarrito() {
        return "CAR-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }

    private String generarIdItem() {
        return "ITEM-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }
}
