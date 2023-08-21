package ru.practicum.requests.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.State;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.model.RequestStatus;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.Optional;

public interface RequestRepo extends JpaRepository<Request, Long> {
    default List<Request> findConfirmedRequestsOnEvent(Long eventId) {
        return findAllByEvent_IdAndStatus(eventId, RequestStatus.CONFIRMED);
    }

    List<Request> findAllByEvent_IdAndStatus(Long eventId, RequestStatus state);

    List<Request> findAllByRequester_Id(Long requesterId);

    Optional<Request> findAllByRequester_IdAndId(Long requesterId, Long id);

    List<Request> findAllByIdIn(List<Long> id);

    List<Request> findAllByEvent(Event event);

    Boolean existsAllByRequester_IdAndEvent_Id(Long userId, Long eventId);

    Boolean existsAllByEvent_InitiatorAndEvent_Id(User user, Long eventId);

    List<Request> findAllByEvent_Initiator(User user);
}
