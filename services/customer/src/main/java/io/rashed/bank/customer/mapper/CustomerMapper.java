package io.rashed.bank.customer.mapper;

import io.rashed.bank.common.api.response.CustomerResponse;
import io.rashed.bank.customer.controller.dto.CreateCustomerRequest;
import io.rashed.bank.customer.repository.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(source = "legalId", target = "legalId")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "address", target = "address")
    Customer toCustomer(CreateCustomerRequest customerRequest);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "legalId", target = "legalId")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "address", target = "addressResponse")
    CustomerResponse toCustomerResponse(Customer customer);

}
