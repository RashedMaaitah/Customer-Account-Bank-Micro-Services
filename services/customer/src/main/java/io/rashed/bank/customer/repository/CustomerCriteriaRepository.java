package io.rashed.bank.customer.repository;

import io.rashed.bank.common.repository.criteria.CriteriaRepository;
import io.rashed.bank.customer.repository.entity.Customer;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerCriteriaRepository extends CriteriaRepository<Customer, CustomerSearchCriteria> {
    public CustomerCriteriaRepository(EntityManager entityManager) {
        super(entityManager, new CustomerCriteriaFilter(), Customer.class);
    }
}
