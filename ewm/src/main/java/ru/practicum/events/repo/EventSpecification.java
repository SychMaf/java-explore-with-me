package ru.practicum.events.repo;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.State;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class EventSpecification implements Specification<Event> {
    private final EventCriteria criteria;
    private final List<Predicate> predicateList;

    public EventSpecification(EventCriteria criteria) {
        this.criteria = criteria;
        this.predicateList = new ArrayList<>();
    }


    @Override
    public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (criteria.getText() != null) {
            Predicate annotation = criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), "%" + criteria.getText().toLowerCase() + "%");
            Predicate description = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + criteria.getText().toLowerCase() + "%");
            Predicate combination = criteriaBuilder.or(annotation, description);
            this.predicateList.add(combination);
        }

        if (criteria.getCategories() != null) {
            Predicate category = root.get("category").in(criteria.getCategories());
            this.predicateList.add(category);
        }

        if (criteria.getPaid() != null) {
            Predicate paid = criteriaBuilder.equal(root.get("paid"), criteria.getPaid());
            this.predicateList.add(paid);
        }

        if (criteria.getRangeStart() != null) {
            Predicate start = criteriaBuilder.greaterThan(root.get("eventDate"), criteria.getRangeStart());
            this.predicateList.add(start);
        }

        if (criteria.getRangeEnd() != null) {
            Predicate end = criteriaBuilder.lessThan(root.get("eventDate"), criteria.getRangeEnd());
            this.predicateList.add(end);
        }

        this.predicateList.add(root.get("state").in(State.PUBLISHED));

        return query.where(criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()])))
                .orderBy(criteriaBuilder.desc(root.get("eventDate")))
                .getRestriction();
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression.toLowerCase());
    }
}
