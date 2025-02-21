package io.rashed.bank.account.repository;

import io.rashed.bank.account.repository.entity.Account;
import io.rashed.bank.common.repository.criteria.CriteriaRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class AccountCriteriaRepository  extends CriteriaRepository<Account, AccountSearchCriteria> {
    public AccountCriteriaRepository(EntityManager entityManager) {
        super(entityManager, new AccountCriteriaFilter(), Account.class);
    }
}
