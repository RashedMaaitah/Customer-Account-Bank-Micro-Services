package io.rashed.bank.customer;

import io.rashed.bank.common.api.response.ApiResponse;
import io.rashed.bank.common.api.response.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "customer-service",
        url = "http://localhost:9090/api/v1/customers"
)
public interface CustomerClient {
    @GetMapping("{id}")
    ApiResponse<CustomerResponse> findCustomerById(@PathVariable("id") String customerId);

}
