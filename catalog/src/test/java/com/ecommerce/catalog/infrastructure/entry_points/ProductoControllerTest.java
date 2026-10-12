package com.ecommerce.catalog.infrastructure.entry_points;

import com.ecommerce.catalog.domain.exception.RecursoNoEncontradoException;
import com.ecommerce.catalog.domain.model.Producto;
import com.ecommerce.catalog.domain.usecase.ProductoUseCase;
import com.ecommerce.catalog.infrastructure.entry_points.dto.ProductoDTO;
import com.ecommerce.catalog.infrastructure.exception.GlobalExceptionHandler;
import com.ecommerce.catalog.infrastructure.mapper.ProductoMapper;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductoUseCase productoUseCase;

    @Spy
    private ProductoMapper productoMapper;

    @InjectMocks
    private ProductoController productoController;

    private Producto producto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        producto = Producto.builder()
                .idProducto("PROD-1234")
                .nombre("Gorra Urbana")
                .descripcion("Gorra negra ajustable")
                .precio(new BigDecimal("25.00"))
                .stock(20)
                .build();
    }

    @Test
    @DisplayName("POST /api/ecommerce/producto debe retornar 201 Created")
    void guardarProducto_retorna201() throws Exception {
        when(productoUseCase.guardarProducto(any(Producto.class))).thenReturn(producto);

        String jsonBody = """
                {
                    "nombre": "Gorra Urbana",
                    "descripcion": "Gorra negra ajustable",
                    "precio": 25.00,
                    "stock": 20
                }
                """;

        mockMvc.perform(post("/api/ecommerce/producto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idProducto").value("PROD-1234"))
                .andExpect(jsonPath("$.nombre").value("Gorra Urbana"));
    }

    @Test
    @DisplayName("GET /api/ecommerce/producto/{id} debe retornar 200 OK")
    void buscarPorId_retorna200() throws Exception {
        when(productoUseCase.buscarPorId("PROD-1234")).thenReturn(producto);

        mockMvc.perform(get("/api/ecommerce/producto/PROD-1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto").value("PROD-1234"))
                .andExpect(jsonPath("$.nombre").value("Gorra Urbana"));
    }

    @Test
    @DisplayName("GET /api/ecommerce/producto/{id} no encontrado retorna 200 con mensaje (según estándar auth)")
    void buscarPorId_noEncontrado() throws Exception {
        when(productoUseCase.buscarPorId("PROD-404"))
                .thenThrow(new RecursoNoEncontradoException("Producto no encontrado"));

        mockMvc.perform(get("/api/ecommerce/producto/PROD-404"))
                .andExpect(status().isOk())
                .andExpect(content().string("Producto no encontrado"));
    }

    @Test
    @DisplayName("GET /api/ecommerce/producto debe retornar lista de productos")
    void listarTodos_retorna200() throws Exception {
        when(productoUseCase.listarProductos()).thenReturn(List.of(producto));

        mockMvc.perform(get("/api/ecommerce/producto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].idProducto").value("PROD-1234"));
    }

    @Test
    @DisplayName("PUT /api/ecommerce/producto debe retornar 200 OK")
    void actualizarProducto_retorna200() throws Exception {
        when(productoUseCase.actualizarProducto(any(Producto.class))).thenReturn(producto);

        String jsonBody = """
                {
                    "idProducto": "PROD-1234",
                    "nombre": "Gorra Urbana Modificada",
                    "precio": 30.00,
                    "stock": 15
                }
                """;

        mockMvc.perform(put("/api/ecommerce/producto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto").value("PROD-1234"));
    }

    @Test
    @DisplayName("DELETE /api/ecommerce/producto/{id} debe retornar 200 OK")
    void eliminarProducto_retorna200() throws Exception {
        doNothing().when(productoUseCase).eliminarProducto("PROD-1234");

        mockMvc.perform(delete("/api/ecommerce/producto/PROD-1234"))
                .andExpect(status().isOk())
                .andExpect(content().string("Producto eliminado exitosamente"));
    }
}
