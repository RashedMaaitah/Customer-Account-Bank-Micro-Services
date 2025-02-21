package io.rashed.bank.account.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.rashed.bank.account.repository.entity.Account;

import java.math.BigDecimal;

public record AccountResponse(
        @JsonProperty("account-id") String accountId,
        @JsonProperty BigDecimal balance,
        @JsonProperty("account-type") Account.AccountType accountType,
        @JsonProperty("status") Account.AccountStatus status
) {
}
