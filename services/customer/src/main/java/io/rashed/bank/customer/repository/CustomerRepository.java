package io.rashed.bank.customer.repository;

import io.rashed.bank.customer.repository.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
