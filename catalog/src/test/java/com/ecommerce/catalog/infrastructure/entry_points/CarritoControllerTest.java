package com.ecommerce.catalog.infrastructure.entry_points;

import com.ecommerce.catalog.domain.model.Carrito;
import com.ecommerce.catalog.domain.model.ItemCarrito;
import com.ecommerce.catalog.domain.usecase.CarritoUseCase;
import com.ecommerce.catalog.infrastructure.exception.GlobalExceptionHandler;
import com.ecommerce.catalog.infrastructure.mapper.CarritoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CarritoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CarritoUseCase carritoUseCase;

    @Spy
    private CarritoMapper carritoMapper;

    @InjectMocks
    private CarritoController carritoController;

    private Carrito carrito;
    private ItemCarrito item;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(carritoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        item = ItemCarrito.builder()
                .idItem("ITEM-999")
                .idCarrito("CAR-123")
                .idProducto("PROD-888")
                .cantidad(2)
                .precioUnitario(new BigDecimal("45.00"))
                .build();

        carrito = Carrito.builder()
                .idCarrito("CAR-123")
                .idUsuario("USR-001")
                .estado("ACTIVO")
                .fechaCreacion(LocalDateTime.now())
                .items(new ArrayList<>(List.of(item)))
                .build();
    }

    @Test
    @DisplayName("GET /api/ecommerce/carrito/usuario/{idUsuario} retorna 200 OK")
    void obtenerCarritoActivoPorUsuario_retorna200() throws Exception {
        when(carritoUseCase.obtenerOCrearCarritoActivo("USR-001")).thenReturn(carrito);

        mockMvc.perform(get("/api/ecommerce/carrito/usuario/USR-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCarrito").value("CAR-123"))
                .andExpect(jsonPath("$.idUsuario").value("USR-001"))
                .andExpect(jsonPath("$.items.length()").value(1));
    }

    @Test
    @DisplayName("GET /api/ecommerce/carrito/{id} retorna 200 OK")
    void buscarPorId_retorna200() throws Exception {
        when(carritoUseCase.buscarPorId("CAR-123")).thenReturn(carrito);

        mockMvc.perform(get("/api/ecommerce/carrito/CAR-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCarrito").value("CAR-123"));
    }

    @Test
    @DisplayName("POST /api/ecommerce/carrito/item agrega ítem y retorna 200 OK")
    void agregarItem_retorna200() throws Exception {
        when(carritoUseCase.agregarItem(anyString(), anyString(), anyInt())).thenReturn(carrito);

        String jsonBody = """
                {
                    "idUsuario": "USR-001",
                    "idProducto": "PROD-888",
                    "cantidad": 2
                }
                """;

        mockMvc.perform(post("/api/ecommerce/carrito/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCarrito").value("CAR-123"));
    }

    @Test
    @DisplayName("DELETE /api/ecommerce/carrito/{idCarrito}/item/{idItem} retorna 200 OK")
    void eliminarItem_retorna200() throws Exception {

        mockMvc.perform(delete("/api/ecommerce/carrito/CAR-123/item/ITEM-999"))
                .andExpect(status().isOk())
                .andExpect(content().string("Item ITEM-999eliminado del carrito CAR-123"));

    }



    @Test
    @DisplayName("POST /api/ecommerce/carrito/{idCarrito}/checkout retorna 200 OK y resumen de compra")
    void checkout_retorna200() throws Exception {
        carrito.setEstado("VENDIDO");
        when(carritoUseCase.checkout("CAR-123")).thenReturn(carrito);

        mockMvc.perform(post("/api/ecommerce/carrito/CAR-123/checkout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCarrito").value("CAR-123"))
                .andExpect(jsonPath("$.estado").value("VENDIDO"))
                .andExpect(jsonPath("$.total").value(90.00))
                .andExpect(jsonPath("$.mensaje").value("Compra realizada exitosamente y saldo descontado"));
    }
}
