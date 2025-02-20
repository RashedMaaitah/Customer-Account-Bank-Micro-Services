package io.rashed.bank.common.repository.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public interface CriteriaFilter<T> {
    Predicate getPredicate(T searchCriteria, Root<?> root, CriteriaBuilder criteriaBuilder);
}