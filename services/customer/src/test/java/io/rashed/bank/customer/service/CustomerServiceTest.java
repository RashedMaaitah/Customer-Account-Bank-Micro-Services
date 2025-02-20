package io.rashed.bank.customer.service;

import io.rashed.bank.common.api.request.PageDTO;
import io.rashed.bank.customer.controller.dto.AddressRequest;
import io.rashed.bank.customer.controller.dto.CreateCustomerRequest;
import io.rashed.bank.customer.controller.dto.UpdateCustomerRequest;
import io.rashed.bank.customer.mapper.CustomerMapper;
import io.rashed.bank.customer.repository.*;
import io.rashed.bank.exception.CustomerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerCriteriaRepository customerCriteriaRepository;
    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    private CreateCustomerRequest createCustomerRequest;
    private Customer customer;
    List<Customer> customers;

    @BeforeEach
    public void setUp() {
        createCustomerRequest = new CreateCustomerRequest(
                "123",
                "test-user",
                Customer.CustomerType.RETAIL,
                new AddressRequest("Jordan", "Amman", "street", "110011")
        );
        customer = Customer.builder()
                .id(1L)
                .legalId("123")
                .type(Customer.CustomerType.RETAIL)
                .username("test-user")
                .address(new Address(1L, "Jordan", "Amman", "street", "110011"))
                .build();

        customers = generateCustomers();
    }


    @Test
    public void createCustomer_WithValidCreateCustomerRequest_ShouldReturnCustomer() {
        when(customerMapper.toCustomer(createCustomerRequest)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);


        Customer result = customerService.createCustomer(createCustomerRequest);

        verify(customerMapper, times(1)).toCustomer(createCustomerRequest);
        verify(customerRepository, times(1)).save(customer);

        assertEquals(result, customer);

    }

    @Test
    public void getCustomer_WithValidCustomerId_ShouldReturnCustomer() {
        Long id = 1L;
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        Customer foundCustomer = customerService.getCustomer(id);

        assertEquals(foundCustomer, customer);
    }

    @Test
    public void updateCustomer_WithValidUpdateCustomerRequestAndValidCustomerId_ShouldUpdateCustomer() {
        Long id = 1L;
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        UpdateCustomerRequest request =
                new UpdateCustomerRequest("123", Customer.CustomerType.CORPORATE, new AddressRequest(
                        "New Country", "New City", "street", "new zip code"
                ));


        customerService.updateCustomer(id, request);

        assertEquals(request.legalId(), customer.getLegalId());
        assertEquals(request.type(), customer.getType());
        assertEquals(request.address().city(), customer.getAddress().getCity());
        assertEquals(request.address().country(), customer.getAddress().getCountry());
        assertEquals(request.address().street(), customer.getAddress().getStreet());
        assertEquals(request.address().zipCode(), customer.getAddress().getZipCode());

        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void getCustomers_WithValidPageDtoAndSearchCriteria_ShouldReturnPageOfCustomersSortedById() {
        // Arrange
        Page<Customer> expectedPage =
                new PageImpl<>(
                        customers.stream().sorted(Comparator.comparing(Customer::getId)).toList(),
                        Pageable.unpaged(),
                        customers.size());

        PageDTO pageDTO = new PageDTO(0, 10, Sort.Direction.ASC, "id");
        CustomerSearchCriteria searchCriteria = new CustomerSearchCriteria(null, null, null, null);

        when(customerCriteriaRepository.findAllWithFilters(pageDTO, searchCriteria)).thenReturn(expectedPage);

        // Act
        Page<Customer> result = customerService.getCustomers(pageDTO, searchCriteria);

        // Assert
        assertNotNull(result);
        assertEquals(expectedPage.getContent().size(), result.getContent().size());
        assertTrue(() -> {
            Long minId = -1L;
            for (var c : result.getContent()) {
                if (c.getId() < minId) {
                    return false;
                }
                minId = c.getId();
            }
            return true;
        });

        verify(customerCriteriaRepository, times(1)).findAllWithFilters(pageDTO, searchCriteria);
    }

    @Test
    public void getCustomer_WithNonExistentCustomerId_ShouldThrowCustomerNotFoundException() {
        Long id = 99L;
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomer(id));
    }

    @Test
    public void updateCustomer_WithNonExistentCustomerId_ShouldThrowCustomerNotFoundException() {
        Long id = 99L;
        UpdateCustomerRequest request = new UpdateCustomerRequest("999", Customer.CustomerType.CORPORATE,
                new AddressRequest("New Country", "New City", "New Street", "999999"));

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.updateCustomer(id, request));
    }

    @Test
    public void getCustomers_WithInvalidSearchCriteria_ShouldThrowIllegalArgumentException() {
        PageDTO pageDTO = new PageDTO(0, 10, Sort.Direction.ASC, "id");
        CustomerSearchCriteria invalidSearchCriteria = new CustomerSearchCriteria("Invalid", "NonExistent", Customer.CustomerType.RETAIL, null);

        when(customerCriteriaRepository.findAllWithFilters(pageDTO, invalidSearchCriteria))
                .thenThrow(new IllegalArgumentException("Invalid search criteria"));

        assertThrows(IllegalArgumentException.class, () -> customerService.getCustomers(pageDTO, invalidSearchCriteria));
    }

    private List<Customer> generateCustomers() {
        return List.of(
                Customer.builder()
                        .id(1L)
                        .legalId("12345")
                        .type(Customer.CustomerType.RETAIL)
                        .username("john.doe")
                        .address(new Address(1L, "Jordan", "Amman", "Main Street", "110011"))
                        .build(),

                Customer.builder()
                        .id(2L)
                        .legalId("67890")
                        .type(Customer.CustomerType.RETAIL)
                        .username("jane.smith")
                        .address(new Address(2L, "Jordan", "Irbid", "Queen Noor St", "211000"))
                        .build(),

                Customer.builder()
                        .id(3L)
                        .legalId("11223")
                        .type(Customer.CustomerType.CORPORATE)
                        .username("alice.johnson")
                        .address(new Address(3L, "Jordan", "Zarqa", "Industrial Zone", "331200"))
                        .build(),

                Customer.builder()
                        .id(4L)
                        .legalId("44556")
                        .type(Customer.CustomerType.CORPORATE)
                        .username("bob.martin")
                        .address(new Address(4L, "Jordan", "Aqaba", "Beach Road", "431300"))
                        .build(),

                Customer.builder()
                        .id(5L)
                        .legalId("78901")
                        .type(Customer.CustomerType.INVESTMENT)
                        .username("emily.white")
                        .address(new Address(5L, "Jordan", "Amman", "King Hussein St", "120100"))
                        .build()
        );
    }

}