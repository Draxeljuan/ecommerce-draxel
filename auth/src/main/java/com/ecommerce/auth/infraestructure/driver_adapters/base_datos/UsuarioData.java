package com.ecommerce.auth.infraestructure.driver_adapters.base_datos;

import jakarta.persistence.*;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "usuarios")
public class UsuarioData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String idUsuario;
    private String nombre;
    @Column(length = 30, nullable = false)
    private String email;
    @Column(length = 255, nullable = false)
    private String pass;
    private String role;
    private Integer edad;
    private String numeroTelefono;
    @ColumnDefault("0.00")
    @Column(name = "saldo", precision = 10, scale = 2)
    private BigDecimal saldo;


}
