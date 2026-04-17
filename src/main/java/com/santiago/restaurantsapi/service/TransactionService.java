package com.santiago.restaurantsapi.service;

import java.util.List;

import com.santiago.restaurantsapi.DTOs.TransactionDTO;
import com.santiago.restaurantsapi.model.ActionType;
import com.santiago.restaurantsapi.model.User;

public interface TransactionService {
    void saveTransaction(ActionType action, User user);
    List<TransactionDTO> getUserTransactions(User user);
}