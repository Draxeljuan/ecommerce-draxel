package com.ecommerce.catalog.infrastructure.driver_adapters.base_datos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoDataJpaRepository extends JpaRepository<ProductoData, String> {

    Optional<ProductoData> findByNombreIgnoreCase(String nombre);


}
