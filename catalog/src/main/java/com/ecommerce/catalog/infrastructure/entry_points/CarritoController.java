package com.ecommerce.catalog.infrastructure.entry_points;

import com.ecommerce.catalog.domain.model.Carrito;
import com.ecommerce.catalog.domain.usecase.CarritoUseCase;
import com.ecommerce.catalog.infrastructure.entry_points.dto.AgregarItemDTO;
import com.ecommerce.catalog.infrastructure.entry_points.dto.CarritoDTO;
import com.ecommerce.catalog.infrastructure.entry_points.dto.CheckoutResponseDTO;
import com.ecommerce.catalog.infrastructure.mapper.CarritoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/ecommerce/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoUseCase carritoUseCase;
    private final CarritoMapper carritoMapper;

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<CarritoDTO> obtenerCarritoActivoPorUsuario(@PathVariable String idUsuario) {
        Carrito carrito = carritoUseCase.obtenerOCrearCarritoActivo(idUsuario);
        return ResponseEntity.ok(carritoMapper.carritoToDto(carrito));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarritoDTO> buscarPorId(@PathVariable String id) {
        Carrito carrito = carritoUseCase.buscarPorId(id);
        return ResponseEntity.ok(carritoMapper.carritoToDto(carrito));
    }

    @PostMapping("/item")
    public ResponseEntity<CarritoDTO> agregarItem(@RequestBody @Valid AgregarItemDTO dto) {
        Carrito carritoActualizado = carritoUseCase.agregarItem(dto.idUsuario(), dto.idProducto(), dto.cantidad());
        return ResponseEntity.ok(carritoMapper.carritoToDto(carritoActualizado));
    }

    @DeleteMapping("/{idCarrito}/item/{idItem}")
    public ResponseEntity<String> eliminarItem(@PathVariable String idCarrito, @PathVariable String idItem) {
        carritoUseCase.eliminarItem(idCarrito, idItem);
        return ResponseEntity.ok().body("Item " + idItem + " eliminado del carrito " + idCarrito);
    }

    @PostMapping("/{idCarrito}/checkout")
    public ResponseEntity<CheckoutResponseDTO> checkout(@PathVariable String idCarrito) {
        Carrito carritoVendido = carritoUseCase.checkout(idCarrito);

        return ResponseEntity.ok(
                carritoMapper.toCheckoutResponseDto(carritoVendido, "Compra realizada exitosamente y saldo descontado")
        );
    }
}
