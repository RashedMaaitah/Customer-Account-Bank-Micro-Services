package io.rashed.bank.account.mapper;

import io.rashed.bank.account.controller.dto.AccountResponse;
import io.rashed.bank.account.repository.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountResponse toAccountResponse(Account account);
}
