package io.rashed.bank.customer.repository;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
//    @ToString.Exclude
    private Long id;

    @NotBlank
    @Column(name = "legal_id", nullable = false, unique = true)
    private String legalId;

    @NotBlank
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CustomerType type;

    @NotNull
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
