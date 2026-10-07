package com.ecommerce.auth.infraestructure.exception;

import com.ecommerce.auth.domain.exception.RecursoNoEncontradoException;
import com.ecommerce.auth.domain.exception.ReglaNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Recursos no encontrados -> Devuelve 200 Aunque no encuentre alguna cosa
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<String> handleRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.OK).body(ex.getMessage());
    }

    // Errores de reglas de negocio / datos inválidos -> Devuelve 400 Bad Request
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<String> handleReglaNegocio(ReglaNegocioException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


}
