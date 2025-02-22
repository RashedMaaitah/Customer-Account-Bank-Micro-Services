package io.rashed.bank.account.service;

import io.rashed.bank.account.controller.dto.CreateAccountRequest;
import io.rashed.bank.account.controller.dto.UpdateAccountRequest;
import io.rashed.bank.account.controller.dto.UpdateAccountStatusRequest;
import io.rashed.bank.account.factory.AccountFactory;
import io.rashed.bank.account.repository.AccountCriteriaRepository;
import io.rashed.bank.account.repository.AccountRepository;
import io.rashed.bank.account.repository.AccountSearchCriteria;
import io.rashed.bank.account.repository.entity.Account;
import io.rashed.bank.common.api.request.PageDTO;
import io.rashed.bank.common.api.response.ApiResponse;
import io.rashed.bank.common.api.response.CustomerResponse;
import io.rashed.bank.common.exception.customer.CustomerNotFoundException;
import io.rashed.bank.customer.CustomerClient;
import io.rashed.bank.exception.AccountLimitExceededException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final CustomerClient customerClient;
    private final AccountCriteriaRepository accountCriteriaRepository;

    @Transactional
    public Account createAccount(CreateAccountRequest request) {
        // TODO make sure the authenticated use only does it or if they have admin access

        getCustomerById(request.customerId());

        long customerAccountsCount = accountRepository.countByCustomerId(request.customerId());
        if (customerAccountsCount >= 10) {
            throw new AccountLimitExceededException("A customer can have no more than 10 accounts.");
        }

        if (request.accountType().equals(Account.AccountType.SALARY)) {
            long customerSalaryAccountCount =
                    accountRepository.countByCustomerIdAndAccountType(request.customerId(), request.accountType());
            if (customerSalaryAccountCount > 0) {
                throw new AccountLimitExceededException("A customer may have one Salary account only.");
            }
        }

        Account account = AccountFactory.createAccount(request, customerAccountsCount + 1);

        return accountRepository.save(account);
    }

    public Page<Account> getAccounts(PageDTO pageDTO, AccountSearchCriteria searchCriteria) {
        return accountCriteriaRepository.findAllWithFilters(pageDTO, searchCriteria);
    }

    @Transactional
    public void updateAccountStatus(String accountId, UpdateAccountStatusRequest request) {
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Invalid AccountId"));

        account.setStatus(request.status());
        accountRepository.save(account);
    }

    @Transactional
    public void deleteAccount(String accountId) {
        accountRepository.deleteAccountByAccountId(accountId);
    }

    @Transactional
    public Account updateAccount(String accountId, UpdateAccountRequest updateRequest) {
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Invalid AccountId"));

        if (Objects.nonNull(updateRequest.balance())) {
            account.setBalance(updateRequest.balance());
        }
        if (Objects.nonNull(updateRequest.status())) {
            account.setStatus(updateRequest.status());
        }

        return accountRepository.save(account);
    }

    @Transactional
    public void deleteCustomerAccounts(Long customerId) {
        accountRepository.deleteByCustomerId(customerId);
    }

    private CustomerResponse getCustomerById(Long customerId) {
        ApiResponse<CustomerResponse> customerApiResponse = customerClient.findCustomerById(customerId.toString());
        if (customerApiResponse == null) {
            throw new RuntimeException();
        }
        CustomerResponse customerResponse = customerApiResponse.getData().getFirst();
        if (Objects.isNull(customerResponse)) {
            throw new CustomerNotFoundException("No customer found with the given id = " + customerId);
        }
        return customerResponse;
    }

}
