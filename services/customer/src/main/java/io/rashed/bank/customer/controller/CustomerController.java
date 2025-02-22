package io.rashed.bank.customer.controller;

import io.rashed.bank.common.api.request.PageDTO;
import io.rashed.bank.common.api.response.ApiResponse;
import io.rashed.bank.common.api.response.CustomerResponse;
import io.rashed.bank.customer.controller.dto.CreateCustomerRequest;
import io.rashed.bank.customer.controller.dto.UpdateCustomerRequest;
import io.rashed.bank.customer.mapper.CustomerMapper;
import io.rashed.bank.customer.repository.entity.Customer;
import io.rashed.bank.customer.repository.CustomerSearchCriteria;
import io.rashed.bank.customer.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @PreAuthorize("hasAnyRole('client_admin')")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<CustomerResponse>>> getCustomersWithFiltersAndSorting(
            PageDTO pageDTO,
            CustomerSearchCriteria searchCriteria
    ) {
        Page<CustomerResponse> customersPageDto =
                customerService.getCustomers(pageDTO, searchCriteria)
                        .map(customerMapper::toCustomerResponse);

        return ResponseEntity.ok(
                ApiResponse.success(List.of(customersPageDto), "Fetched customers"));
    }


    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(
            @RequestBody @Valid CreateCustomerRequest customerRequest
    ) {
        String username = getSecurityContextUsername();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                                List.of(customerMapper.toCustomerResponse(
                                        customerService.createCustomer(customerRequest, username))),
                                "Customer Created Successfully"
                        )
                );
    }

    @PreAuthorize("hasAnyRole('client_admin')")
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomer(
            @PathVariable @NotNull Long id
    ) {
        Customer customer = customerService.getCustomer(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        List.of(customerMapper.toCustomerResponse(customer)),
                        "Customer Retrieved Successfully"
                )
        );
    }

    @PreAuthorize("hasAnyRole('client_admin')")
    @PutMapping("{id}")
    public ResponseEntity<Void> updateCustomer(
            @PathVariable @NotNull Long id,
            @RequestBody @Valid UpdateCustomerRequest customerRequest
    ) {
        customerService.updateCustomer(id, customerRequest);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('client_admin')")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable @NotNull Long id
    ) {
        customerService.deleteCustomer(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<CustomerResponse>> getAuthenticatedCustomer() {
        String username = getSecurityContextUsername();
        Customer customer = customerService.getCustomer(username);
        return ResponseEntity.ok(
                ApiResponse.success(
                        List.of(customerMapper.toCustomerResponse(customer)),
                        "Customer Retrieved Successfully"
                )
        );
    }


    private String getSecurityContextUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (Objects.isNull(username)) {
            throw new AccessDeniedException("Access denied");
        }
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}












