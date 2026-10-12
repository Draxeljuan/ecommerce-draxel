package com.ecommerce.catalog.domain.usecase;

import com.ecommerce.catalog.domain.exception.ReglaNegocioException;
import com.ecommerce.catalog.domain.model.Carrito;
import com.ecommerce.catalog.domain.model.ItemCarrito;
import com.ecommerce.catalog.domain.model.Producto;
import com.ecommerce.catalog.domain.model.gateway.CarritoGateway;
import com.ecommerce.catalog.domain.model.gateway.ProductoGateway;
import com.ecommerce.catalog.domain.model.gateway.UsuarioAuthGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoUseCaseTest {

    @Mock
    private CarritoGateway carritoGateway;

    @Mock
    private ProductoGateway productoGateway;

    @Mock
    private UsuarioAuthGateway usuarioAuthGateway;

    @InjectMocks
    private CarritoUseCase carritoUseCase;

    private Producto productoEjemplo;
    private Carrito carritoEjemplo;
    private ItemCarrito itemEjemplo;

    @BeforeEach
    void setUp() {
        productoEjemplo = Producto.builder()
                .idProducto("PROD-001")
                .nombre("Zapatos")
                .precio(new BigDecimal("100.00"))
                .stock(5)
                .build();

        itemEjemplo = ItemCarrito.builder()
                .idItem("ITEM-001")
                .idCarrito("CAR-001")
                .idProducto("PROD-001")
                .cantidad(2)
                .precioUnitario(new BigDecimal("100.00"))
                .build();

        carritoEjemplo = Carrito.builder()
                .idCarrito("CAR-001")
                .idUsuario("USR-123")
                .estado("ACTIVO")
                .fechaCreacion(LocalDateTime.now())
                .items(new ArrayList<>(List.of(itemEjemplo)))
                .build();
    }

    @Test
    @DisplayName("Debe retornar carrito activo existente con sus ítems")
    void obtenerOCrearCarritoActivo_existente() {
        when(carritoGateway.buscarActivoPorUsuario("USR-123")).thenReturn(carritoEjemplo);
        when(carritoGateway.buscarItemsPorCarrito("CAR-001")).thenReturn(List.of(itemEjemplo));

        Carrito resultado = carritoUseCase.obtenerOCrearCarritoActivo("USR-123");

        assertNotNull(resultado);
        assertEquals("CAR-001", resultado.getIdCarrito());
        assertEquals("ACTIVO", resultado.getEstado());
        assertEquals(1, resultado.getItems().size());
    }

    @Test
    @DisplayName("Debe crear un nuevo carrito activo si el usuario no tiene uno")
    void obtenerOCrearCarritoActivo_nuevo() {
        when(carritoGateway.buscarActivoPorUsuario("USR-456")).thenReturn(null);
        when(carritoGateway.guardarCarrito(any(Carrito.class))).thenAnswer(i -> i.getArgument(0));

        Carrito resultado = carritoUseCase.obtenerOCrearCarritoActivo("USR-456");

        assertNotNull(resultado);
        assertTrue(resultado.getIdCarrito().startsWith("CAR-"));
        assertEquals("USR-456", resultado.getIdUsuario());
        assertEquals("ACTIVO", resultado.getEstado());
    }

    @Test
    @DisplayName("Debe agregar un nuevo ítem al carrito si hay stock suficiente")
    void agregarItem_nuevoItem_exito() {
        when(productoGateway.buscarPorId("PROD-001")).thenReturn(productoEjemplo);
        when(carritoGateway.buscarActivoPorUsuario("USR-123")).thenReturn(carritoEjemplo);
        when(carritoGateway.buscarItemPorCarritoYProducto("CAR-001", "PROD-001")).thenReturn(null);
        when(carritoGateway.buscarPorId("CAR-001")).thenReturn(carritoEjemplo);
        when(carritoGateway.buscarItemsPorCarrito("CAR-001")).thenReturn(List.of(itemEjemplo));

        Carrito resultado = carritoUseCase.agregarItem("USR-123", "PROD-001", 3);

        assertNotNull(resultado);
        verify(carritoGateway).guardarItem(any(ItemCarrito.class));
    }

    @Test
    @DisplayName("Debe incrementar cantidad si el ítem ya existe en el carrito")
    void agregarItem_itemExistente_incrementaCantidad() {
        when(productoGateway.buscarPorId("PROD-001")).thenReturn(productoEjemplo);
        when(carritoGateway.buscarActivoPorUsuario("USR-123")).thenReturn(carritoEjemplo);
        when(carritoGateway.buscarItemPorCarritoYProducto("CAR-001", "PROD-001")).thenReturn(itemEjemplo);
        when(carritoGateway.buscarPorId("CAR-001")).thenReturn(carritoEjemplo);
        when(carritoGateway.buscarItemsPorCarrito("CAR-001")).thenReturn(List.of(itemEjemplo));

        // Tenía 2, sumamos 2 = 4 (stock es 5)
        Carrito resultado = carritoUseCase.agregarItem("USR-123", "PROD-001", 2);

        assertNotNull(resultado);
        assertEquals(4, itemEjemplo.getCantidad());
        verify(carritoGateway).guardarItem(itemEjemplo);
    }

    @Test
    @DisplayName("Debe lanzar ReglaNegocioException si la cantidad solicitada supera el stock")
    void agregarItem_superaStock() {
        when(productoGateway.buscarPorId("PROD-001")).thenReturn(productoEjemplo);
        when(carritoGateway.buscarActivoPorUsuario("USR-123")).thenReturn(carritoEjemplo);
        when(carritoGateway.buscarItemPorCarritoYProducto("CAR-001", "PROD-001")).thenReturn(null);

        // Pedimos 6, stock es 5
        assertThrows(ReglaNegocioException.class, () -> carritoUseCase.agregarItem("USR-123", "PROD-001", 6));
        verify(carritoGateway, never()).guardarItem(any());
    }

    @Test
    @DisplayName("Debe eliminar un ítem del carrito")
    void eliminarItem_exito() {

        when(carritoGateway.buscarPorId("CAR-001")).thenReturn(carritoEjemplo); // carritoEjemplo tiene estado "ACTIVO"
        when(carritoGateway.buscarItemPorId("ITEM-001")).thenReturn(itemEjemplo); // itemEjemplo tiene idCarrito "CAR-001"

        carritoUseCase.eliminarItem("CAR-001", "ITEM-001");

        verify(carritoGateway).eliminarItem("ITEM-001");
    }


    @Test
    @DisplayName("Debe realizar checkout exitosamente: descuenta stock, saldo y marca VENDIDO")
    void checkout_exito() {
        // Carrito con 2 zapatos a 100.00 = Total 200.00
        when(carritoGateway.buscarPorId("CAR-001")).thenReturn(carritoEjemplo);
        when(carritoGateway.buscarItemsPorCarrito("CAR-001")).thenReturn(List.of(itemEjemplo));
        when(productoGateway.buscarPorId("PROD-001")).thenReturn(productoEjemplo);
        when(usuarioAuthGateway.obtenerSaldoUsuario("USR-123")).thenReturn(new BigDecimal("300.00"));
        when(carritoGateway.guardarCarrito(any(Carrito.class))).thenAnswer(i -> i.getArgument(0));

        Carrito vendido = carritoUseCase.checkout("CAR-001");

        assertNotNull(vendido);
        assertEquals("VENDIDO", vendido.getEstado());

        // Stock original 5, se vendieron 2 -> Quedan 3
        assertEquals(3, productoEjemplo.getStock());
        verify(productoGateway).actualizarProducto(productoEjemplo);

        // Saldo original 300, total 200 -> Nuevo saldo 100
        verify(usuarioAuthGateway).actualizarSaldo("USR-123", new BigDecimal("100.00"));
        verify(carritoGateway).guardarCarrito(carritoEjemplo);
    }

    @Test
    @DisplayName("Debe fallar el checkout si el carrito no está ACTIVO")
    void checkout_carritoNoActivo() {
        carritoEjemplo.setEstado("VENDIDO");
        when(carritoGateway.buscarPorId("CAR-001")).thenReturn(carritoEjemplo);

        assertThrows(ReglaNegocioException.class, () -> carritoUseCase.checkout("CAR-001"));
        verify(usuarioAuthGateway, never()).actualizarSaldo(any(), any());
    }

    @Test
    @DisplayName("Debe fallar el checkout si el carrito no tiene ítems")
    void checkout_carritoVacio() {
        carritoEjemplo.setItems(new ArrayList<>());
        when(carritoGateway.buscarPorId("CAR-001")).thenReturn(carritoEjemplo);
        when(carritoGateway.buscarItemsPorCarrito("CAR-001")).thenReturn(new ArrayList<>());

        assertThrows(ReglaNegocioException.class, () -> carritoUseCase.checkout("CAR-001"));
    }

    @Test
    @DisplayName("Debe fallar el checkout si el saldo del usuario es insuficiente")
    void checkout_saldoInsuficiente() {
        when(carritoGateway.buscarPorId("CAR-001")).thenReturn(carritoEjemplo);
        when(carritoGateway.buscarItemsPorCarrito("CAR-001")).thenReturn(List.of(itemEjemplo));
        when(productoGateway.buscarPorId("PROD-001")).thenReturn(productoEjemplo);
        // Total es 200.00, pero usuario tiene solo 50.00
        when(usuarioAuthGateway.obtenerSaldoUsuario("USR-123")).thenReturn(new BigDecimal("50.00"));

        assertThrows(ReglaNegocioException.class, () -> carritoUseCase.checkout("CAR-001"));
        verify(usuarioAuthGateway, never()).actualizarSaldo(any(), any());
        verify(productoGateway, never()).actualizarProducto(any());
    }

    @Test
    @DisplayName("Debe fallar el checkout si el stock no es suficiente")
    void checkout_stockInsuficiente() {
        productoEjemplo.setStock(1); // Se necesitan 2
        when(carritoGateway.buscarPorId("CAR-001")).thenReturn(carritoEjemplo);
        when(carritoGateway.buscarItemsPorCarrito("CAR-001")).thenReturn(List.of(itemEjemplo));
        when(productoGateway.buscarPorId("PROD-001")).thenReturn(productoEjemplo);

        assertThrows(ReglaNegocioException.class, () -> carritoUseCase.checkout("CAR-001"));
        verify(usuarioAuthGateway, never()).actualizarSaldo(any(), any());
    }
}
