package io.rashed.bank.account.controller;

import io.rashed.bank.account.controller.dto.AccountResponse;
import io.rashed.bank.account.controller.dto.CreateAccountRequest;
import io.rashed.bank.account.controller.dto.UpdateAccountRequest;
import io.rashed.bank.account.controller.dto.UpdateAccountStatusRequest;
import io.rashed.bank.account.mapper.AccountMapper;
import io.rashed.bank.account.repository.AccountSearchCriteria;
import io.rashed.bank.account.repository.entity.Account;
import io.rashed.bank.account.service.AccountService;
import io.rashed.bank.common.api.request.PageDTO;
import io.rashed.bank.common.api.response.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.QueryParam;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.NotImplementedException;
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
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Account>>> getCustomersWithFiltersAndSorting(
            PageDTO pageDTO,
            AccountSearchCriteria searchCriteria
    ) {
        Page<Account> accountPage =
                accountService.getAccounts(pageDTO, searchCriteria);

        return ResponseEntity.ok(
                ApiResponse.success(List.of(accountPage), "Fetched accounts"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(
            @RequestBody @Valid CreateAccountRequest request
    ) {
        AccountResponse accountResponse =
                accountMapper.toAccountResponse(accountService.createAccount(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        List.of(accountResponse),
                        "Created account successfully"
                ));
    }

    @PatchMapping("{account-id}")
    public ResponseEntity<Void> updateAccountStatus(
            @PathVariable("account-id")
            @NotNull
            @Size(min = 10, max = 10, message = "Account Id must be 10 digits")
            String accountId,
            @RequestBody
            @NotNull
            UpdateAccountStatusRequest updateAccountStatusRequest
    ) {
        accountService.updateAccountStatus(accountId, updateAccountStatusRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("me")
    public ResponseEntity<List<Account>> getCurrentUserAccounts() {
        // TODO
        throw new NotImplementedException("getCurrentUserAccounts === /me");
    }

    @DeleteMapping("{account-id}")
    public ResponseEntity<Void> deleteAccount(
            @PathVariable("account-id")
            @NotNull
            @Size(min = 10, max = 10, message = "Account Id must be 10 digits")
            String accountId
    ) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{account-id}")
    public ResponseEntity<ApiResponse<AccountResponse>> updateAccount(
            @PathVariable("account-id")
            @NotNull
            @Size(min = 10, max = 10, message = "Account Id must be 10 digits")
            String accountId,

            @RequestBody
            @Valid
            UpdateAccountRequest updateRequest
    ) {

        AccountResponse accountResponse = accountMapper.toAccountResponse(
                accountService.updateAccount(accountId, updateRequest)
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        List.of(accountResponse),
                        "Updated account successfully"
                )
        );
    }

    
    @PreAuthorize("hasAnyRole('client_admin')")
    @DeleteMapping
    public ResponseEntity<Void> deleteCustomerAccounts(
            @QueryParam("customerId")
            @NotNull(message = "customer-id can't be null")
            Long customerId
    ) {
        accountService.deleteCustomerAccounts(customerId);
        return ResponseEntity.noContent().build();
    }

    private String getSecurityContextUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (Objects.isNull(username)) {
            throw new AccessDeniedException("Access denied");
        }
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
