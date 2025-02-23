package io.rashed.bank.transaction.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long transactionId,
        String message,
        String sourceAccountId,
        String destinationAccountId, // Null for deposits/withdrawals
        BigDecimal amount,
        LocalDateTime timestamp
) {
}