package ru.practicum.events.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.events.model.Event;
import org.dtoPoint.events.State;

import java.util.List;
import java.util.Optional;

public interface EventRepo extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    default Optional<Event> findPublishedEventById(Long eventId) {
        return findAllByIdAndState(eventId, State.PUBLISHED);
    }

    List<Event> findAllByInitiator_Id(Long id, Pageable pageable);

    Optional<Event> findAllByIdAndInitiator_Id(Long id, Long userId);

    Optional<Event> findAllByIdAndState(Long eventId, State state);

    List<Event> findAllByIdIn(List<Long> ids);

    Boolean existsByCategory_Id(Long categoryId);
}
