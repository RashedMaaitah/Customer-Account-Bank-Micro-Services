package io.rashed.bank.customer.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.rashed.bank.customer.controller.dto.AddressRequest;

public record CustomerSearchCriteria(
        @JsonProperty
        String username,

        @JsonProperty("legal_id")
        String legalId,

        @JsonProperty
        Customer.CustomerType type,

        @JsonProperty("address")
        AddressRequest addressRequest
) {

}
