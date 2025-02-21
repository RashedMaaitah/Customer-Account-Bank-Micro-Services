package io.rashed.bank.common.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.rashed.bank.common.enums.customer.CustomerType;

public record CustomerResponse(
        @JsonProperty String username,
        @JsonProperty(value = "legal_id") String legalId,
        @JsonProperty CustomerType type,
        @JsonProperty(value = "address") AddressResponse addressResponse
) {
}
