package io.rashed.bank.customer.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.rashed.bank.customer.repository.Customer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCustomerRequest(
        @JsonProperty("legal_id")
        @NotBlank(message = "legal_id is required")
        String legalId,

        @NotBlank(message = "username is required")
        @JsonProperty String username,

        @JsonProperty
        @NotNull(message = "Customer must be assigned a type")
        Customer.CustomerType type,

        @Valid
        @JsonProperty
        AddressRequest address
) {
}
