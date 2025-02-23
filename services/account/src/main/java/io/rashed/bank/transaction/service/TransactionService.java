package io.rashed.bank.transaction.service;

import io.rashed.bank.account.repository.AccountRepository;
import io.rashed.bank.account.repository.entity.Account;
import io.rashed.bank.transaction.controller.dto.TransactionRequest;
import io.rashed.bank.transaction.controller.dto.TransactionResponse;
import io.rashed.bank.transaction.repository.Transaction;
import io.rashed.bank.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public TransactionResponse createTransaction(TransactionRequest request) {
        Account source = getAccount(request.sourceAccountId());

        // If the transaction type is a withdrawal or transfer, verify if sufficient balance exists
        if ((request.type() == Transaction.TransactionType.WITHDRAWAL || request.type() == Transaction.TransactionType.TRANSFER)
            && source.getBalance().compareTo(request.amount()) < 0) {
            return new TransactionResponse(
                    null,
                    "Insufficient balance",
                    request.sourceAccountId(),
                    null,
                    null,
                    LocalDateTime.now()
            );
        }

        // Handle deposit
        if (request.type() == Transaction.TransactionType.DEPOSIT) {
            source.setBalance(source.getBalance().add(request.amount()));
            accountRepository.save(source);
            Transaction transaction = saveTransaction(request, null);
            return toResponse(transaction, "Deposit successful");
        }

        // Handle withdrawal
        if (request.type() == Transaction.TransactionType.WITHDRAWAL) {
            source.setBalance(source.getBalance().subtract(request.amount()));
            accountRepository.save(source);
            Transaction transaction = saveTransaction(request, null);
            return toResponse(transaction, "Withdrawal successful");
        }

        // Handle transfer
        if (request.type() == Transaction.TransactionType.TRANSFER) {
            Account destination = getAccount(request.destinationAccountId());

            source.setBalance(source.getBalance().subtract(request.amount()));
            destination.setBalance(destination.getBalance().add(request.amount()));

            accountRepository.save(source);
            accountRepository.save(destination);

            Transaction transaction = saveTransaction(request, request.destinationAccountId());
            return toResponse(transaction, "Transfer successful");
        }

        return new TransactionResponse(
                null,
                "Invalid transaction type",
                request.sourceAccountId(),
                null,
                null,
                LocalDateTime.now()
        );
    }

    public List<TransactionResponse> getTransactionsByAccountId(String accountId) {
        return transactionRepository.findBySourceAccountIdOrDestinationAccountId(accountId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private Account getAccount(String accountId) {
        return accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    private Transaction saveTransaction(TransactionRequest request, String destinationId) {
        // Create and save the transaction
        Transaction transaction = Transaction.builder()
                .type(request.type())
                .sourceAccount(getAccount(request.sourceAccountId()))
                .destinationAccount(destinationId != null ? getAccount(destinationId) : null)
                .amount(request.amount())
                .timestamp(LocalDateTime.now())
                .build();
        return transactionRepository.save(transaction);
    }

    private TransactionResponse toResponse(Transaction transaction, String message) {
        return new TransactionResponse(
                transaction.getId(),
                message,
                transaction.getSourceAccount().getAccountId(),
                transaction.getDestinationAccount() != null ? transaction.getDestinationAccount().getAccountId() : null,
                transaction.getAmount(),
                transaction.getTimestamp()
        );
    }

    private TransactionResponse toResponse(Transaction transaction) {
        return toResponse(transaction, "Transaction retrieved successfully");
    }
}