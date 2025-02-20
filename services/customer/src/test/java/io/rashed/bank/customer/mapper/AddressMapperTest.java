package io.rashed.bank.customer.mapper;

import io.rashed.bank.customer.controller.dto.AddressRequest;
import io.rashed.bank.customer.controller.dto.AddressResponse;
import io.rashed.bank.customer.repository.Address;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class AddressMapperTest {

    private final AddressMapper mapper = Mappers.getMapper(AddressMapper.class);


    @Test
    public void toAddress_WithNullRequest_ShouldReturnNull() {
        Address address = mapper.toAddress(null);
        assertThat(address).isNull();
    }

    @Test
    public void toAddress_WithIncompleteAddressRequest_ShouldMapToAddress() {
        AddressRequest addressRequest = new AddressRequest(
                null, // Missing country
                "Amman",
                "Street",
                null  // Missing zip code
        );

        Address address = mapper.toAddress(addressRequest);

        assertThat(address).isNotNull();
        assertThat(address.getCountry()).isNull();
        assertThat(address.getCity()).isEqualTo("Amman");
        assertThat(address.getStreet()).isEqualTo("Street");
        assertThat(address.getZipCode()).isNull();
    }

    @Test
    public void toAddressResponse_WithValidAddress_ShouldMapToResponse() {
        Address address = new Address();
        address.setCountry("NY");
        address.setStreet("456 Elm St");
        address.setCity("Metropolis");
        address.setZipCode("10001");

        AddressResponse response = mapper.toAddressResponse(address);

        assertThat(response).isNotNull();
        assertThat(response.street()).isEqualTo("456 Elm St");
        assertThat(response.city()).isEqualTo("Metropolis");
        assertThat(response.country()).isEqualTo("NY");
        assertThat(response.zipCode()).isEqualTo("10001");
    }

    @Test
    public void toAddressResponse_WithNullAddress_ShouldReturnNull() {
        AddressResponse response = mapper.toAddressResponse(null);
        assertThat(response).isNull();
    }

    @Test
    public void toAddressResponse_WithIncompleteAddress_ShouldMapToResponse() {
        Address address = new Address();
        address.setCity("Metropolis");
        address.setStreet(null);  // Missing street
        address.setZipCode("10001");

        AddressResponse response = mapper.toAddressResponse(address);

        assertThat(response).isNotNull();
        assertThat(response.street()).isNull();
        assertThat(response.city()).isEqualTo("Metropolis");
        assertThat(response.country()).isNull(); // Country missing
        assertThat(response.zipCode()).isEqualTo("10001");
    }

    @Test
    public void toAddress_WithValidAddressRequest_ShouldMapToAddress() {
        AddressRequest addressRequest = new AddressRequest(
                "Jordan",
                "Amman",
                "Street",
                "zipcode"
        );

        Address address = mapper.toAddress(addressRequest);

        assertThat(address).isNotNull();
        assertThat(address.getCountry()).isEqualTo(addressRequest.country());
        assertThat(address.getCity()).isEqualTo(addressRequest.city());
        assertThat(address.getStreet()).isEqualTo(addressRequest.street());
        assertThat(address.getZipCode()).isEqualTo(addressRequest.zipCode());
    }

    @Test
    public void toAddressResponse() {
        // Arrange
        Address address = new Address();
        address.setCountry("NY");
        address.setStreet("456 Elm St");
        address.setCity("Metropolis");
        address.setZipCode("10001");

        // Act
        AddressResponse response = mapper.toAddressResponse(address);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.street()).isEqualTo("456 Elm St");
        assertThat(response.city()).isEqualTo("Metropolis");
        assertThat(response.country()).isEqualTo("NY");
        assertThat(response.zipCode()).isEqualTo("10001");
    }
}