package com.santiago.restaurantsapi.Java;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.santiago.restaurantsapi.DTOs.TransactionDTO;
import com.santiago.restaurantsapi.model.ActionType;
import com.santiago.restaurantsapi.model.Transaction;
import com.santiago.restaurantsapi.model.User;
import com.santiago.restaurantsapi.repository.TransactionRepository;
import com.santiago.restaurantsapi.service.TransactionServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    @DisplayName("Debe guardar una acción en la lista de transacciones del usuario")
    public void testSaveTransaction_Successful() {
        User user = User.builder()
                .id(1L)
                .email("santiago@test.com")
                .firstName("Santiago")
                .lastName("Guerrero")
                .age(23)
                .build();

        transactionService.saveTransaction(ActionType.LOGIN, user);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository, times(1)).save(transactionCaptor.capture());

        Transaction savedTransaction = transactionCaptor.getValue();

        assertNotNull(savedTransaction);
        assertEquals(ActionType.LOGIN, savedTransaction.getAction());
        assertEquals(user, savedTransaction.getUser());
        assertNotNull(savedTransaction.getDate());
    }

    @Test
    @DisplayName("Debe retornar las transacciones de un usuario")
    public void testGetUserTransactions_Successful() {
        User user = User.builder()
                .id(1L)
                .email("santiago@test.com")
                .firstName("Santiago")
                .lastName("Guerrero")
                .age(23)
                .build();

        LocalDateTime fecha1 = LocalDateTime.of(2026, 4, 17, 10, 0);
        LocalDateTime fecha2 = LocalDateTime.of(2026, 4, 17, 11, 0);

        Transaction transaction1 = Transaction.builder()
                .id(1L)
                .action(ActionType.LOGIN)
                .date(fecha1)
                .user(user)
                .build();

        Transaction transaction2 = Transaction.builder()
                .id(2L)
                .action(ActionType.CONSULTAR_RESTAURANTES)
                .date(fecha2)
                .user(user)
                .build();

        when(transactionRepository.findByUserOrderByDateDesc(user))
                .thenReturn(List.of(transaction2, transaction1));

        List<TransactionDTO> result = transactionService.getUserTransactions(user);

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(ActionType.CONSULTAR_RESTAURANTES, result.get(0).getAction());
        assertEquals(fecha2, result.get(0).getDate());

        assertEquals(ActionType.LOGIN, result.get(1).getAction());
        assertEquals(fecha1, result.get(1).getDate());

        verify(transactionRepository, times(1)).findByUserOrderByDateDesc(user);
    }

}
