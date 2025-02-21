package io.rashed.bank.exception;

public class AccountLimitExceededException extends RuntimeException {
    public AccountLimitExceededException(String msg) {
        super(msg);
    }
}
