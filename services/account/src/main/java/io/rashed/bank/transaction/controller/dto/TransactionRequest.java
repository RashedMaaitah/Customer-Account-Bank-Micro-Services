package io.rashed.bank.transaction.controller.dto;

import io.rashed.bank.transaction.repository.Transaction;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransactionRequest(
        @NotNull(message = "Transaction type is required")
        Transaction.TransactionType type,

        @NotNull(message = "Source account ID is required")
        @Size(min = 10, max = 10, message = "sourceAccountId Account id must be 10 digits")
        String sourceAccountId,

        @Size(min = 10, max = 10, message = "Destination Account id must be 10 digits")
        String destinationAccountId, // Required only for transfers

        @NotNull(message = "Amount is required")
        @Min(value = 1, message = "Amount must be greater than zero")
        BigDecimal amount
) {
}