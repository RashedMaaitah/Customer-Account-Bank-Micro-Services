package io.rashed.bank.common.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AddressResponse(
        @JsonProperty String country,
        @JsonProperty String city,
        @JsonProperty(value = "street_name") String street,
        @JsonProperty(value = "zip_code") String zipCode
) {
}