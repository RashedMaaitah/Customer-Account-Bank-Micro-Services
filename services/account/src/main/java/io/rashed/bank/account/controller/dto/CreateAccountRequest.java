package io.rashed.bank.account.controller.dto;

import io.rashed.bank.account.repository.entity.Account;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateAccountRequest(
        @NotNull(message = "Customer ID cannot be null")
        Long customerId,

        @NotNull(message = "Balance cannot be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "Balance must be greater than zero")
        @Digits(integer = 10, fraction = 2, message = "Balance must be a valid monetary amount with up to 2 decimal places")
        BigDecimal balance,

        @NotNull(message = "Account type is required")
        Account.AccountType accountType
) {
}
