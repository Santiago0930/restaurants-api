package com.santiago.restaurantsapi.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.santiago.restaurantsapi.DTOs.TransactionDTO;
import com.santiago.restaurantsapi.model.ActionType;
import com.santiago.restaurantsapi.model.Transaction;
import com.santiago.restaurantsapi.model.User;
import com.santiago.restaurantsapi.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void saveTransaction(ActionType action, User user) {
        Transaction transaction = Transaction.builder()
                .action(action)
                .date(LocalDateTime.now())
                .user(user)
                .build();

        transactionRepository.save(transaction);
    }

    @Override
    public List<TransactionDTO> getUserTransactions(User user) {
        return transactionRepository.findByUserOrderByDateDesc(user)
                .stream()
                .map(t -> new TransactionDTO(t.getAction(), t.getDate()))
                .toList();
    }
}