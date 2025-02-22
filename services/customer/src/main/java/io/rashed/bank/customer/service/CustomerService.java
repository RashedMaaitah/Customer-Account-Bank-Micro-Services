package io.rashed.bank.customer.service;

import io.rashed.bank.common.api.request.PageDTO;
import io.rashed.bank.common.exception.EntityAlreadyExistsException;
import io.rashed.bank.customer.controller.dto.CreateCustomerRequest;
import io.rashed.bank.customer.controller.dto.UpdateCustomerRequest;
import io.rashed.bank.customer.mapper.CustomerMapper;
import io.rashed.bank.customer.repository.*;
import io.rashed.bank.customer.repository.entity.Address;
import io.rashed.bank.customer.repository.entity.Customer;
import io.rashed.bank.exception.CustomerNotFoundException;
import io.rashed.bank.producer.RabbitMQProducer;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final RabbitMQProducer rabbitMQProducer;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final CustomerCriteriaRepository customerCriteriaRepository;

    public Customer getCustomer(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(
                        format("Customer with id = %d doesn't exist", id)
                ));
    }

    public Customer getCustomer(String username) {
        return customerRepository.findByUsername(username)
                .orElseThrow(() -> new CustomerNotFoundException(
                        format("Customer with username = %s doesn't exist", username)
                ));
    }

    @Transactional
    public Customer createCustomer(CreateCustomerRequest customerRequest, String username) {
        customerRepository.findByUsername(username)
                .ifPresent((c) -> {
                    throw new EntityAlreadyExistsException("username", username, Customer.class);
                });

        Customer customer = customerMapper.toCustomer(customerRequest);
        customer.setUsername(username);
        Customer saved = customerRepository.save(customer);
        saved.setId(null);
        return saved;
    }

    @Transactional
    public void updateCustomer(Long id, UpdateCustomerRequest customerRequest) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(
                        format("Can't update customer -- No customer found with the given id = %d", id)
                ));
        mergeCustomer(customer, customerRequest);
        customerRepository.save(customer);
    }

    public Page<Customer> getCustomers(PageDTO pageDTO, CustomerSearchCriteria searchCriteria) {
        return customerCriteriaRepository.findAllWithFilters(pageDTO, searchCriteria);
    }

    @Transactional
    public void deleteCustomer(@NotNull Long id) {
        rabbitMQProducer.sendDeleteCustomerAccounts(id);
        customerRepository.deleteById(id);
    }

    private void mergeCustomer(Customer customer, UpdateCustomerRequest request) {
        if (StringUtils.isNotBlank(request.legalId())) {
            customer.setLegalId(request.legalId());
        }
        if (Objects.nonNull(request.type())) {
            customer.setType(request.type());
        }
        Address address = customer.getAddress();
        if (StringUtils.isNotBlank(request.address().city())) {
            address.setCity(request.address().city());
        }
        if (StringUtils.isNotBlank(request.address().country())) {
            address.setCountry(request.address().country());
        }
        if (StringUtils.isNotBlank(request.address().street())) {
            address.setStreet(request.address().street());
        }
        if (StringUtils.isNotBlank(request.address().zipCode())) {
            address.setZipCode(request.address().zipCode());
        }
        customer.setAddress(address);
    }

}
