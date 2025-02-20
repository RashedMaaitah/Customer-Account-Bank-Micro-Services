package io.rashed.bank.customer.mapper;

import io.rashed.bank.customer.controller.dto.AddressRequest;
import io.rashed.bank.customer.controller.dto.AddressResponse;
import io.rashed.bank.customer.controller.dto.CreateCustomerRequest;
import io.rashed.bank.customer.controller.dto.CustomerResponse;
import io.rashed.bank.customer.repository.Address;
import io.rashed.bank.customer.repository.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerMapperTest {

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private final CustomerMapper customerMapper = Mappers.getMapper(CustomerMapper.class);


    @Test
    public void toCustomer_WithNullRequest_ShouldReturnNull() {
        // Act
        Customer customer = customerMapper.toCustomer(null);

        // Assert
        assertThat(customer).isNull();
    }

    @Test
    public void toCustomer_WithNullAddressRequest_ShouldMapWithoutAddress() {
        // Arrange
        CreateCustomerRequest request = new CreateCustomerRequest(
                "123456789",
                "john_doe",
                Customer.CustomerType.RETAIL,
                null // Address is null
        );

        // Act
        Customer customer = customerMapper.toCustomer(request);

        // Assert
        assertThat(customer).isNotNull();
        assertThat(customer.getLegalId()).isEqualTo("123456789");
        assertThat(customer.getUsername()).isEqualTo("john_doe");
        assertThat(customer.getType()).isEqualTo(Customer.CustomerType.RETAIL);
        assertThat(customer.getAddress()).isNull();
    }

    @Test
    public void toCustomerResponse_WithNullCustomer_ShouldReturnNull() {
        // Act
        CustomerResponse response = customerMapper.toCustomerResponse(null);

        // Assert
        assertThat(response).isNull();
    }

    @Test
    public void toCustomerResponse_WithNullAddress_ShouldMapWithoutAddressResponse() {
        // Arrange
        Customer customer = new Customer();
        customer.setLegalId("987654321");
        customer.setUsername("jane_doe");
        customer.setType(Customer.CustomerType.RETAIL);
        customer.setAddress(null); // Address is null

        // Act
        CustomerResponse response = customerMapper.toCustomerResponse(customer);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.legalId()).isEqualTo("987654321");
        assertThat(response.username()).isEqualTo("jane_doe");
        assertThat(response.type()).isEqualTo(Customer.CustomerType.RETAIL);
        assertThat(response.addressResponse()).isNull();
    }

    @Test
    public void toCustomer_WithValidCreateCustomerRequest_ShouldMapToCustomer() {
        // Arrange
        AddressRequest addressRequest = new AddressRequest(
                "IL",
                "Springfield",
                "123 Main St",
                "62704"
        );

        Address mockAddress = new Address();
        mockAddress.setStreet("123 Main St");
        mockAddress.setCity("Springfield");
        mockAddress.setCountry("IL");
        mockAddress.setZipCode("62704");

        when(addressMapper.toAddress(addressRequest)).thenReturn(mockAddress);

        CreateCustomerRequest request = new CreateCustomerRequest(
                "123456789",
                "john_doe",
                Customer.CustomerType.RETAIL,
                addressRequest
        );

        // Act
        Customer customer = customerMapper.toCustomer(request);

        // Assert
        assertThat(customer).isNotNull();
        assertThat(customer.getLegalId()).isEqualTo("123456789");
        assertThat(customer.getUsername()).isEqualTo("john_doe");
        assertThat(customer.getType()).isEqualTo(Customer.CustomerType.RETAIL);

        // Validate Address Mapping
        assertThat(customer.getAddress()).isNotNull();
        assertThat(customer.getAddress().getStreet()).isEqualTo("123 Main St");
        assertThat(customer.getAddress().getCity()).isEqualTo("Springfield");
        assertThat(customer.getAddress().getCountry()).isEqualTo("IL");
        assertThat(customer.getAddress().getZipCode()).isEqualTo("62704");
    }

    @Test
    public void toCustomerResponse_WithValidCustomer_ShouldMapToCustomerResponse() {
        // Arrange
        Address address = new Address();
        address.setStreet("456 Elm St");
        address.setCity("Metropolis");
        address.setCountry("NY");
        address.setZipCode("10001");

        AddressResponse mockAddressResponse = new AddressResponse(
                "NY",
                "Metropolis",
                "456 Elm St",
                "10001"
        );

        when(addressMapper.toAddressResponse(address)).thenReturn(mockAddressResponse);

        Customer customer = new Customer();
        customer.setLegalId("987654321");
        customer.setUsername("jane_doe");
        customer.setType(Customer.CustomerType.RETAIL);
        customer.setAddress(address);

        // Act
        CustomerResponse response = customerMapper.toCustomerResponse(customer);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.legalId()).isEqualTo("987654321");
        assertThat(response.username()).isEqualTo("jane_doe");
        assertThat(response.type()).isEqualTo(Customer.CustomerType.RETAIL);

        // Validate Address Response Mapping
        assertThat(response.addressResponse()).isNotNull();
        assertThat(response.addressResponse().street()).isEqualTo("456 Elm St");
        assertThat(response.addressResponse().city()).isEqualTo("Metropolis");
        assertThat(response.addressResponse().country()).isEqualTo("NY");
        assertThat(response.addressResponse().zipCode()).isEqualTo("10001");
    }
}
