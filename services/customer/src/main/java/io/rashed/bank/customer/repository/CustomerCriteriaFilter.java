package io.rashed.bank.customer.repository;

import io.rashed.bank.common.repository.criteria.CriteriaFilter;
import io.rashed.bank.customer.repository.entity.Address;
import io.rashed.bank.customer.repository.entity.Customer;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomerCriteriaFilter implements CriteriaFilter<CustomerSearchCriteria> {

    @Override
    public Predicate getPredicate(CustomerSearchCriteria searchCriteria,
                                  Root<?> root,
                                  CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(searchCriteria.username())) {
            predicates.add(
                    criteriaBuilder.like(root.get("username"), like(searchCriteria.username()))
            );
        }

        if (StringUtils.isNotBlank(searchCriteria.legalId())) {
            predicates.add(
                    criteriaBuilder.like(root.get("legalId"), like(searchCriteria.legalId()))
            );
        }

        if (Objects.nonNull(searchCriteria.type())) {
            predicates.add(
                    criteriaBuilder.like(root.get("type"), searchCriteria.type().toString())
            );
        }

        if (Objects.nonNull(searchCriteria.addressRequest())) {
            Join<Customer, Address> customerAddressJoin = root.join("customer");

            if (StringUtils.isNotBlank(searchCriteria.addressRequest().country())) {
                predicates.add(
                        criteriaBuilder.like(customerAddressJoin.get("country"), like(searchCriteria.addressRequest()
                                .country()))
                );
            }
            if (StringUtils.isNotBlank(searchCriteria.addressRequest().city())) {
                predicates.add(
                        criteriaBuilder.like(customerAddressJoin.get("city"), like(searchCriteria.addressRequest()
                                .city()))
                );
            }
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private String like(String field) {
        return "%" + field + "%";
    }
}
