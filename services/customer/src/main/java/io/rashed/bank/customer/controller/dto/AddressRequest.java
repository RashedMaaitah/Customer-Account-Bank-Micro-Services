package io.rashed.bank.customer.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequest(
        @NotBlank(message = "Country cannot be blank")
        @Size(max = 50, message = "Country name cannot exceed 50 characters")
        @JsonProperty
        String country,

        @NotBlank(message = "City cannot be blank")
        @Size(max = 50, message = "City name cannot exceed 50 characters")
        @JsonProperty
        String city,

        @NotBlank(message = "Street name cannot be blank")
        @Size(max = 100, message = "Street name cannot exceed 100 characters")
        @JsonProperty("street_name")
        String street,

        @NotBlank(message = "ZIP code cannot be blank")
        @Size(min = 4, max = 10, message = "ZIP code must be between 4 and 10 characters")
        @JsonProperty("zip_code")
        String zipCode
) {
}
