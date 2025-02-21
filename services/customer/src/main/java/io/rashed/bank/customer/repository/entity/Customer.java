package io.rashed.bank.customer.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @NotBlank(message = "Legal ID cannot be blank")
    @Size(min = 5, max = 20, message = "Legal ID must be between 5 and 20 characters")
    @Column(name = "legal_id", nullable = false, unique = true)
    private String legalId;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotNull(message = "Customer type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CustomerType type;

    @NotNull(message = "Address cannot be null")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @ToString.Include(name = "customerId", rank = 1)
    public String getPaddedCustomerId() {
        return String.format("%07d", id);  // Pad with leading zeros to make 7 digits
    }

    public enum CustomerType {
        RETAIL,
        CORPORATE,
        INVESTMENT
    }
}
