package io.rashed.bank.customer.controller;

import io.rashed.bank.common.api.request.PageDTO;
import io.rashed.bank.common.api.response.ApiResponse;
import io.rashed.bank.customer.controller.dto.CreateCustomerRequest;
import io.rashed.bank.customer.controller.dto.CustomerResponse;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CustomerResponse>>> getCustomersWithFiltersAndSorting(
            PageDTO pageDTO,
            CustomerSearchCriteria searchCriteria
    ) {
        Page<CustomerResponse> productDTOPage =
                customerService.getCustomers(pageDTO, searchCriteria)
                        .map(customerMapper::toCustomerResponse);

        return ResponseEntity.ok(
                ApiResponse.success(List.of(productDTOPage), "Fetched products"));
    }


    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(
            @RequestBody @Valid CreateCustomerRequest customerRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                                List.of(customerMapper.toCustomerResponse(
                                        customerService.createCustomer(customerRequest))),
                                "Customer Created Successfully"
                        )
                );
    }

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

    @PutMapping("{id}")
    public ResponseEntity<Void> updateCustomer(
            @PathVariable @NotNull Long id,
            @RequestBody @Valid UpdateCustomerRequest customerRequest
    ) {
        customerService.updateCustomer(id, customerRequest);
        return ResponseEntity.noContent().build();
    }
}












