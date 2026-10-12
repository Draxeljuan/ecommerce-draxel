package com.ecommerce.catalog.infrastructure.driver_adapters.base_datos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemCarritoDataJpaRepository extends JpaRepository<ItemCarritoData, String> {
    List<ItemCarritoData> findByIdCarrito(String idCarrito);
    Optional<ItemCarritoData> findByIdCarritoAndIdProducto(String idCarrito, String idProducto);
    boolean existsByIdProducto(String idProducto);
}
