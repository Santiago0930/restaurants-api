package com.santiago.restaurantsapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.santiago.restaurantsapi.DTOs.TransactionDTO;
import com.santiago.restaurantsapi.model.User;
import com.santiago.restaurantsapi.service.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Obtiene el historial de transacciones del usuario autenticado.
     *
     * @param authentication Información del usuario autenticado.
     * @return Lista de transacciones realizadas por el usuario.
     */

    @GetMapping("/me")
    public ResponseEntity<List<TransactionDTO>> getMyTransactions(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(transactionService.getUserTransactions(user));
    }
}