package ru.practicum.validator;

import lombok.experimental.UtilityClass;
import ru.practicum.exception.exceptions.NotFoundException;
import ru.practicum.rate.repo.RateRepo;
import org.dtoPoint.requests.RequestStatus;
import ru.practicum.requests.repo.RequestRepo;

@UtilityClass
public class RateValidator {
    public void checkUserHaveConfirmedRequest(RequestRepo requestRepo, Long userId, Long eventId) {
        if (!requestRepo.existsByRequester_IdAndEvent_IdAndStatus(userId, eventId, RequestStatus.CONFIRMED)) {
            throw new NotFoundException("User with this param does not exist");
        }
    }

    public void checkLikeExists(RateRepo rateRepo, Long userId, Long eventId) {
        if (!rateRepo.existsByUserAndEvent(eventId, userId)) {
            throw new NotFoundException("Like with this param does not exist");
        }
    }
}
