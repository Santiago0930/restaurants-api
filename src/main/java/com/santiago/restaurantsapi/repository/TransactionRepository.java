package com.santiago.restaurantsapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.santiago.restaurantsapi.model.Transaction;
import com.santiago.restaurantsapi.model.User;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
    List<Transaction> findByUserOrderByDateDesc(User user);
}