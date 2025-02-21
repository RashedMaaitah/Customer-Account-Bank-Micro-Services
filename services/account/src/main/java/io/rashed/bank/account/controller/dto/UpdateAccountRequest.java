package io.rashed.bank.account.controller.dto;

import io.rashed.bank.account.repository.entity.Account;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;

public record UpdateAccountRequest(
        @DecimalMin(value = "0.0", inclusive = false, message = "Balance must be greater than zero")
        @Digits(integer = 10, fraction = 2, message = "Balance must be a valid monetary amount with up to 2 decimal places")
        BigDecimal balance,

        Account.AccountStatus status
) {
}
