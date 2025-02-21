package io.rashed.bank.account.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @NotNull(message = "Account number cannot be null")
    @Size(min = 10, max = 10, message = "Account number must be exactly 10 digits")
    @Column(name = "account_id", nullable = false, unique = true)
    private String accountId;

    @NotNull(message = "Customer ID cannot be null")
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @NotNull(message = "Balance cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Balance must be greater than zero")
    @Digits(integer = 10, fraction = 2, message = "Balance must be a valid monetary amount with up to 2 decimal places")
    @Column(name = "balance", nullable = false, precision = 12, scale = 2)
    private BigDecimal balance;

    @NotNull(message = "Account type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @NotNull(message = "Account status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccountStatus status;

    public enum AccountStatus {
        ACTIVE,
        INACTIVE,
        SUSPENDED
    }

    public enum AccountType {
        SAVINGS,
        INVESTMENT,
        SALARY
    }
}
