package com.santiago.restaurantsapi.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja la excepción BadCredentialsException cuando las credenciales de
     * autenticación son inválidas.
     * 
     * @param ex excepción lanzada cuando el usuario proporciona credenciales
     *           incorrectas
     * @return ResponseEntity con código 401 y mensaje de error "Credenciales
     *         incorrectas"
     */

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
    }

    /**
     * Maneja excepciones de tipo IllegalArgumentException lanzadas en la
     * aplicación.
     *
     * @param ex excepción lanzada cuando se envían argumentos inválidos
     * @return ResponseEntity con código 401 y el mensaje de error correspondiente
     */

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    /**
     * Maneja excepciones de tipo RuntimeException.
     * Captura errores inesperados o de lógica de negocio
     *
     * @param ex excepción de tipo RuntimeException lanzada durante la ejecución
     * @return ResponseEntity con código 400 y el mensaje de la excepción
     */

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}