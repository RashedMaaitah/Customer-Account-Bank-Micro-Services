package io.rashed.bank.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("""
            SELECT t from Transaction t WHERE t.destinationAccount.accountId=:accountId OR t.sourceAccount.accountId=:accountId
            """)
    List<Transaction> findBySourceAccountIdOrDestinationAccountId(String accountId);
}