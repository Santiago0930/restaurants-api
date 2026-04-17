package com.santiago.restaurantsapi.DTOs;

import java.time.LocalDateTime;

import com.santiago.restaurantsapi.model.ActionType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionDTO {
    @Enumerated(EnumType.STRING)
    private ActionType action;
    private LocalDateTime date;
}