package io.rashed.bank.common.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.rashed.bank.common.enums.customer.CustomerType;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record CustomerResponse(
        @JsonProperty(value = "id") Long id,
        @JsonProperty String username,
        @JsonProperty(value = "legal_id") String legalId,
        @JsonProperty CustomerType type,
        @JsonProperty(value = "address") AddressResponse addressResponse
) {
}
