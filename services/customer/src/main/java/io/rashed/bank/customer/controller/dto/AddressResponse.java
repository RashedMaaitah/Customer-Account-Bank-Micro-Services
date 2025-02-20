package io.rashed.bank.customer.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AddressResponse(
        @JsonProperty String country,
        @JsonProperty String city,
        @JsonProperty(value = "street_name") String street,
        @JsonProperty(value = "zip_code") String zipCode
) {
}
