package com.ecommerce.catalog.infrastructure.entry_points;

import com.ecommerce.catalog.domain.model.Producto;
import com.ecommerce.catalog.domain.usecase.ProductoUseCase;
import com.ecommerce.catalog.infrastructure.entry_points.dto.ActualizarProductoRequestDTO;
import com.ecommerce.catalog.infrastructure.entry_points.dto.CrearProductoRequestDTO;
import com.ecommerce.catalog.infrastructure.entry_points.dto.ProductoDTO;
import com.ecommerce.catalog.infrastructure.mapper.ProductoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ecommerce/producto")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoUseCase productoUseCase;
    private final ProductoMapper productoMapper;

    @PostMapping({"", "/save"})
    public ResponseEntity<ProductoDTO> guardarProducto(@RequestBody CrearProductoRequestDTO productoDTO) {
        Producto producto = productoMapper.dtoCrearToProducto(productoDTO);
        Producto productoGuardado = productoUseCase.guardarProducto(producto);
        return new ResponseEntity<>(productoMapper.productoToDto(productoGuardado), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> buscarPorId(@PathVariable String id) {
        Producto producto = productoUseCase.buscarPorId(id);
        return ResponseEntity.ok(productoMapper.productoToDto(producto));
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listarTodos() {
        List<ProductoDTO> productos = productoUseCase.listarProductos().stream()
                .map(productoMapper::productoToDto)
                .toList();
        return ResponseEntity.ok(productos);
    }

    @PutMapping({"", "/update"})
    public ResponseEntity<ProductoDTO> actualizarProducto(@RequestBody ActualizarProductoRequestDTO productoDTO) {
        Producto producto = productoMapper.dtoActualizarToProducto(productoDTO);
        Producto productoActualizado = productoUseCase.actualizarProducto(producto);
        return ResponseEntity.ok(productoMapper.productoToDto(productoActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarProducto(@PathVariable String id) {
        productoUseCase.eliminarProducto(id);
        return ResponseEntity.ok("Producto eliminado exitosamente");
    }
}
