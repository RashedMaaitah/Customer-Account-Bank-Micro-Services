package io.rashed.bank.account.repository;

import io.rashed.bank.common.repository.criteria.CriteriaFilter;
import jakarta.persistence.criteria.*;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccountCriteriaFilter implements CriteriaFilter<AccountSearchCriteria> {

    @Override
    public Predicate getPredicate(AccountSearchCriteria searchCriteria,
                                  Root<?> root,
                                  CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(searchCriteria.accountId())) {
            predicates.add(
                    criteriaBuilder.like(root.get("accountId"), like(searchCriteria.accountId()))
            );
        }

        if (Objects.nonNull(searchCriteria.customerId())) {
            predicates.add(
                    criteriaBuilder.equal(root.get("customerId"), searchCriteria.customerId())
            );
        }

        if (Objects.nonNull(searchCriteria.minBalance())) {
            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(root.get("balance"), searchCriteria.minBalance())
            );
        }

        if (Objects.nonNull(searchCriteria.maxBalance())) {
            predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(root.get("balance"), searchCriteria.maxBalance())
            );
        }

        if (Objects.nonNull(searchCriteria.accountType())) {
            predicates.add(
                    criteriaBuilder.equal(root.get("accountType"), searchCriteria.accountType())
            );
        }

        if (Objects.nonNull(searchCriteria.status())) {
            predicates.add(
                    criteriaBuilder.equal(root.get("status"), searchCriteria.status())
            );
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private String like(String field) {
        return "%" + field.trim() + "%";
    }
}
