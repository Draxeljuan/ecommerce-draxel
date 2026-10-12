package com.ecommerce.catalog.domain.usecase;

import com.ecommerce.catalog.domain.exception.RecursoNoEncontradoException;
import com.ecommerce.catalog.domain.exception.ReglaNegocioException;
import com.ecommerce.catalog.domain.model.Producto;
import com.ecommerce.catalog.domain.model.gateway.ProductoGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoUseCaseTest {

    @Mock
    private ProductoGateway productoGateway;

    @InjectMocks
    private ProductoUseCase productoUseCase;

    private Producto productoEjemplo;

    @BeforeEach
    void setUp() {
        productoEjemplo = Producto.builder()
                .idProducto("PROD-TEST01")
                .nombre("Camiseta Deportiva")
                .descripcion("Camiseta 100% poliéster")
                .precio(new BigDecimal("50.00"))
                .stock(10)
                .build();
    }

    @Test
    @DisplayName("Debe guardar un producto exitosamente y generar ID si no se proporciona")
    void guardarProducto_exito() {
        Producto productoNuevo = Producto.builder()
                .nombre("Pantalón")
                .precio(new BigDecimal("80.00"))
                .stock(5)
                .build();

        when(productoGateway.guardarProducto(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Producto resultado = productoUseCase.guardarProducto(productoNuevo);

        assertNotNull(resultado);
        assertNotNull(resultado.getIdProducto());
        assertTrue(resultado.getIdProducto().startsWith("PROD-"));
        assertEquals("Pantalón", resultado.getNombre());
        assertNotNull(resultado.getFechaCreacion());
        verify(productoGateway).guardarProducto(any(Producto.class));
    }

    @Test
    @DisplayName("Debe lanzar ReglaNegocioException al guardar un producto nulo")
    void guardarProducto_productoNulo() {
        assertThrows(ReglaNegocioException.class, () -> productoUseCase.guardarProducto(null));
        verify(productoGateway, never()).guardarProducto(any());
    }

    @Test
    @DisplayName("Debe lanzar ReglaNegocioException cuando el nombre está vacío")
    void guardarProducto_nombreVacio() {
        productoEjemplo.setNombre("   ");
        assertThrows(ReglaNegocioException.class, () -> productoUseCase.guardarProducto(productoEjemplo));
    }

    @Test
    @DisplayName("Debe lanzar ReglaNegocioException cuando el precio es menor o igual a cero")
    void guardarProducto_precioInvalido() {
        productoEjemplo.setPrecio(BigDecimal.ZERO);
        assertThrows(ReglaNegocioException.class, () -> productoUseCase.guardarProducto(productoEjemplo));

        productoEjemplo.setPrecio(new BigDecimal("-10.00"));
        assertThrows(ReglaNegocioException.class, () -> productoUseCase.guardarProducto(productoEjemplo));
    }

    @Test
    @DisplayName("Debe lanzar ReglaNegocioException cuando el stock es negativo")
    void guardarProducto_stockNegativo() {
        productoEjemplo.setStock(-1);
        assertThrows(ReglaNegocioException.class, () -> productoUseCase.guardarProducto(productoEjemplo));
    }

    @Test
    @DisplayName("Debe buscar producto por ID exitosamente")
    void buscarPorId_exito() {
        when(productoGateway.buscarPorId("PROD-TEST01")).thenReturn(productoEjemplo);

        Producto resultado = productoUseCase.buscarPorId("PROD-TEST01");

        assertNotNull(resultado);
        assertEquals("PROD-TEST01", resultado.getIdProducto());
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException si el producto no existe")
    void buscarPorId_noExiste() {
        when(productoGateway.buscarPorId("PROD-NOEXISTE")).thenReturn(null);

        assertThrows(RecursoNoEncontradoException.class, () -> productoUseCase.buscarPorId("PROD-NOEXISTE"));
    }

    @Test
    @DisplayName("Debe listar todos los productos")
    void listarProductos_exito() {
        when(productoGateway.listarProductos()).thenReturn(List.of(productoEjemplo));

        List<Producto> lista = productoUseCase.listarProductos();

        assertEquals(1, lista.size());
        assertEquals("PROD-TEST01", lista.get(0).getIdProducto());
    }

    @Test
    @DisplayName("Debe actualizar un producto existente exitosamente")
    void actualizarProducto_exito() {
        when(productoGateway.buscarPorId("PROD-TEST01")).thenReturn(productoEjemplo);
        when(productoGateway.actualizarProducto(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        productoEjemplo.setPrecio(new BigDecimal("60.00"));
        Producto actualizado = productoUseCase.actualizarProducto(productoEjemplo);

        assertEquals(new BigDecimal("60.00"), actualizado.getPrecio());
        verify(productoGateway).actualizarProducto(productoEjemplo);
    }

    @Test
    @DisplayName("Debe eliminar un producto existente")
    void eliminarProducto_exito() {
        when(productoGateway.buscarPorId("PROD-TEST01")).thenReturn(productoEjemplo);

        assertDoesNotThrow(() -> productoUseCase.eliminarProducto("PROD-TEST01"));
        verify(productoGateway).eliminarProducto("PROD-TEST01");
    }
}
