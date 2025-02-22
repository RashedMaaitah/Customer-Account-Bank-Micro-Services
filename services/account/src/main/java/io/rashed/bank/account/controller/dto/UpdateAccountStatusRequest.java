package io.rashed.bank.account.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.rashed.bank.account.repository.entity.Account;
import jakarta.validation.constraints.NotNull;

public record UpdateAccountStatusRequest(
        @NotNull(message = "status can't be null")
        @JsonProperty("status")
        Account.AccountStatus status
) {
}
