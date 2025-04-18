package io.rashed.bank.customer.mapper;

import io.rashed.bank.common.api.response.AddressResponse;
import io.rashed.bank.customer.controller.dto.AddressRequest;
import io.rashed.bank.customer.repository.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    Address toAddress(AddressRequest request);

    AddressResponse toAddressResponse(Address address);
}
