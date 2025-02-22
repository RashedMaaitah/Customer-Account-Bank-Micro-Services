package io.rashed.bank.account.repository;

import io.rashed.bank.account.repository.entity.Account;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    long countByCustomerId(@NotNull(message = "Customer ID cannot be null") Long customerId);

    long countByCustomerIdAndAccountType(@NotNull(message = "Customer ID cannot be null") Long customerId, Account.@NotNull(message = "Account type is required") AccountType accountType);

    List<Account> findAllByCustomerId(Long customerId);

    Optional<Account> findByAccountId(String accountId);

    void deleteAccountByAccountId(String accountId);

    void deleteByCustomerId(Long customerId);
}
