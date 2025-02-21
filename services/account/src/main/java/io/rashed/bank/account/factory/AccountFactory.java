package io.rashed.bank.account.factory;

import io.rashed.bank.account.controller.dto.CreateAccountRequest;
import io.rashed.bank.account.repository.entity.Account;

import java.math.BigDecimal;

public abstract class AccountFactory {

    public static final BigDecimal SAVING_ACCOUNT_MIN_BALANCE = new BigDecimal("1000.00");
    public static final BigDecimal INVESTMENT_ACCOUNT_MIN_BALANCE = new BigDecimal("5000.00");
    public static final BigDecimal SALARY_ACCOUNT_MIN_BALANCE = new BigDecimal("0.00");

    public static Account createAccount(CreateAccountRequest createAccountRequest, Long accountNumber) {
        Account.AccountBuilder accountBuilder = Account.builder();

        switch (createAccountRequest.accountType()) {
            case SAVINGS:
                validateSavingsAccount(createAccountRequest.balance());
                accountBuilder.accountType(Account.AccountType.SAVINGS);
                break;
            case INVESTMENT:
                validateInvestmentAccount(createAccountRequest.balance());
                accountBuilder.accountType(Account.AccountType.INVESTMENT);
                break;
            case SALARY:
                validateSalaryAccount(createAccountRequest.balance());
                accountBuilder.accountType(Account.AccountType.SALARY);
                break;
            default:
                throw new IllegalArgumentException("Unsupported account type");
        }

        accountBuilder
                .balance(createAccountRequest.balance())
                .customerId(createAccountRequest.customerId())
                .status(Account.AccountStatus.ACTIVE)
                .accountId(
                        generateAccountId(createAccountRequest.customerId(), accountNumber)
                );

        return accountBuilder.build();
    }

    private static String generateAccountId(Long customerId, Long accountNumber) {
        String formattedCustomerId = String.format("%07d", customerId);
        String formattedAccountNumber = String.format("%03d", accountNumber);

        return formattedCustomerId + formattedAccountNumber;
    }


    private static void validateSavingsAccount(BigDecimal initialBalance) {
        if (initialBalance.compareTo(SAVING_ACCOUNT_MIN_BALANCE) <= 0) {
            throw new IllegalArgumentException("Minimum balance for Savings account is 1000");
        }
    }

    private static void validateInvestmentAccount(BigDecimal initialBalance) {
        if (initialBalance.compareTo(INVESTMENT_ACCOUNT_MIN_BALANCE) <= 0) {
            throw new IllegalArgumentException("Minimum balance for Investment account is 5000");
        }
    }

    private static void validateSalaryAccount(BigDecimal initialBalance) {
        if (initialBalance.compareTo(SALARY_ACCOUNT_MIN_BALANCE) < 0) {
            throw new IllegalArgumentException("Salary account balance cannot be negative");
        }
    }
}
