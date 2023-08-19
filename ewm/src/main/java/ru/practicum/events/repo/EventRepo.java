package ru.practicum.events.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.events.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepo extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiator_Id(Long id, Pageable pageable);

    Optional<Event> findAllByIdAndInitiator_Id(Long id, Long userId);

}
