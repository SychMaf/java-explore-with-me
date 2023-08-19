package ru.practicum.requests.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.requests.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepo extends JpaRepository<Request, Long> {
    List<Request> findAllByRequester_Id(Long requesterId);
    Optional<Request> findAllByRequester_IdAndId(Long requesterId, Long id);
    List<Request> findAllByEvent_IdAndEvent_Initiator_Id(Long userId, Long eventId);
}
