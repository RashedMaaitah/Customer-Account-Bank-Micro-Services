package io.rashed.bank.customer.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record AddressRequest(
        @NotBlank
        @JsonProperty
        String country,

        @NotBlank
        @JsonProperty
        String city,

        @NotBlank
        @JsonProperty(value = "street_name")
        String street,

        @NotBlank
        @JsonProperty(value = "zip_code")
        String zipCode
) {
}
