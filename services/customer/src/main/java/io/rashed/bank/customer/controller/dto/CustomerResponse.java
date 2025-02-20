package io.rashed.bank.customer.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.rashed.bank.customer.repository.Customer;

public record CustomerResponse(
        @JsonProperty String username,
        @JsonProperty(value = "legal_id") String legalId,
        @JsonProperty Customer.CustomerType type,
        @JsonProperty(value = "address") AddressResponse addressResponse
) {
}
