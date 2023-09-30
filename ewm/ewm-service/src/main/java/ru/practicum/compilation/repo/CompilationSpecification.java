package ru.practicum.compilation.repo;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.compilation.model.Compilation;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class CompilationSpecification implements Specification<Compilation> {
    private final CompilationCriteria criteria;
    private final List<Predicate> predicateList;

    public CompilationSpecification(CompilationCriteria criteria) {
        this.criteria = criteria;
        this.predicateList = new ArrayList<>();
    }

    @Override
    public Predicate toPredicate(Root<Compilation> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (criteria.getPinned() != null) {
            Predicate paid = criteriaBuilder.equal(root.get("pinned"), criteria.getPinned());
            this.predicateList.add(paid);
        }

        return query.where(criteriaBuilder.and(predicateList.toArray(new Predicate[0])))
                .getRestriction();
    }
}
