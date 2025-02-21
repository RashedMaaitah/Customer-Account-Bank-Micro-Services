package io.rashed.bank.account.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.rashed.bank.account.repository.entity.Account;

import java.math.BigDecimal;

public record AccountSearchCriteria(
        @JsonProperty String accountId,
        @JsonProperty Long customerId,
        @JsonProperty BigDecimal minBalance,
        @JsonProperty BigDecimal maxBalance,
        @JsonProperty Account.AccountType accountType,
        @JsonProperty Account.AccountStatus status
) {
}
