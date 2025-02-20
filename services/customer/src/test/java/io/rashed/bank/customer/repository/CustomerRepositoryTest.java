package io.rashed.bank.customer.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@EntityScan(basePackages = "io.rashed.bank.customer.repository")
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;
    private Address address;

    @BeforeEach
    void setUp() {
        // Create and set up the Address entity
        address = new Address();
        address.setCountry("JO");
        address.setStreet("123 Main St");
        address.setCity("Springfield");
        address.setZipCode("12345");

        // Create the Customer entity
        customer = Customer.builder()
                .legalId("123456789")
                .username("john_doe")
                .type(Customer.CustomerType.RETAIL)
                .address(address)
                .build();
    }

    @Test
    void save_WithValidCustomer_ShouldSaveSuccessfully() {
        // Act
        Customer savedCustomer = customerRepository.save(customer);

        // Assert
        assertNotNull(savedCustomer.getId()); // Ensures ID is assigned after saving
        assertEquals("123456789", savedCustomer.getLegalId());
        assertEquals("john_doe", savedCustomer.getUsername());
        assertEquals(Customer.CustomerType.RETAIL, savedCustomer.getType());
        assertEquals("123 Main St", savedCustomer.getAddress().getStreet());
        assertEquals("Springfield", savedCustomer.getAddress().getCity());
        assertEquals("JO", savedCustomer.getAddress().getCountry());
        assertEquals("12345", savedCustomer.getAddress().getZipCode());
    }

    @Test
    void findById_WithValidId_ShouldReturnCustomer() {
        // Arrange
        Customer savedCustomer = customerRepository.save(customer);

        // Act
        Optional<Customer> foundCustomer = customerRepository.findById(savedCustomer.getId());

        // Assert
        assertTrue(foundCustomer.isPresent());
        assertEquals(savedCustomer.getId(), foundCustomer.get().getId());
        assertEquals(savedCustomer.getLegalId(), foundCustomer.get().getLegalId());
        assertEquals(savedCustomer.getUsername(), foundCustomer.get().getUsername());
        assertEquals(savedCustomer.getType(), foundCustomer.get().getType());
        assertEquals(savedCustomer.getAddress().getStreet(), foundCustomer.get().getAddress().getStreet());
        assertEquals(savedCustomer.getAddress().getCity(), foundCustomer.get().getAddress().getCity());
        assertEquals(savedCustomer.getAddress().getCountry(), foundCustomer.get().getAddress().getCountry());
        assertEquals(savedCustomer.getAddress().getZipCode(), foundCustomer.get().getAddress().getZipCode());
    }

    @Test
    void findById_WithInvalidId_ShouldReturnEmptyOptional() {
        // Act
        Optional<Customer> foundCustomer = customerRepository.findById(999L);

        // Assert
        assertFalse(foundCustomer.isPresent()); // Ensures that no customer is found for the invalid ID
    }

    @Test
    void save_WithDifferentCustomerTypes_ShouldSaveSuccessfully() {
        // Act: Save a customer with a CORPORATE type
        customer.setType(Customer.CustomerType.CORPORATE);
        Customer savedCustomerCorporate = customerRepository.save(customer);

        // Assert
        assertNotNull(savedCustomerCorporate.getId());
        assertEquals(Customer.CustomerType.CORPORATE, savedCustomerCorporate.getType());

        // Act: Save a customer with an INVESTMENT type
        customer.setType(Customer.CustomerType.INVESTMENT);
        Customer savedCustomerInvestment = customerRepository.save(customer);

        // Assert
        assertNotNull(savedCustomerInvestment.getId());
        assertEquals(Customer.CustomerType.INVESTMENT, savedCustomerInvestment.getType());
    }

    // You can add more tests here based on your requirements
}
