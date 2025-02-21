package io.rashed.bank.customer.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.rashed.bank.common.enums.customer.CustomerType;
import io.rashed.bank.customer.controller.dto.AddressRequest;
import io.rashed.bank.customer.repository.entity.Customer;

public record CustomerSearchCriteria(
        @JsonProperty
        String username,

        @JsonProperty("legal_id")
        String legalId,

        @JsonProperty
        CustomerType type,

        @JsonProperty("address")
        AddressRequest addressRequest
) {

}
