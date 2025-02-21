package io.rashed.bank.customer.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.rashed.bank.common.enums.customer.CustomerType;
import io.rashed.bank.customer.repository.entity.Customer;

public record UpdateCustomerRequest(
        @JsonProperty("legal_id")
        String legalId,

        @JsonProperty
        CustomerType type,

        @JsonProperty
        AddressRequest address
) {
}
