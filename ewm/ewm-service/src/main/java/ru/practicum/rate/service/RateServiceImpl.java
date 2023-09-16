package ru.practicum.rate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.model.Event;
import ru.practicum.events.repo.EventRepo;
import ru.practicum.exception.exceptions.IllegalStateException;
import ru.practicum.exception.exceptions.NotFoundException;
import org.dtoPoint.rate.OutputRateDto;
import org.dtoPoint.rate.PopularEventsDto;
import ru.practicum.rate.dto.RateMapper;
import ru.practicum.rate.model.Rate;
import ru.practicum.rate.model.RatePK;
import org.dtoPoint.rate.StateRate;
import ru.practicum.rate.repo.RateRepo;
import ru.practicum.requests.repo.RequestRepo;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepo;
import ru.practicum.validator.EventValidator;
import ru.practicum.validator.RateValidator;
import ru.practicum.validator.UserValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RateServiceImpl implements RateService {
    private final RequestRepo requestRepo;
    private final RateRepo rateRepo;
    private final EventRepo eventRepo;
    private final UserRepo userRepo;

    @Override
    @Transactional
    public OutputRateDto createLike(Long userId, Long eventId, String rate) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d does not exist"));
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("User dont have events with this id"));
        RateValidator.checkUserHaveConfirmedRequest(requestRepo, userId, eventId);
        RatePK ratePK = RatePK.builder()
                .event(event)
                .user(user)
                .build();
        Rate createdRate = Rate.builder()
                .ratePK(ratePK)
                .build();
        try {
            StateRate stateRate = StateRate.valueOf(rate.toUpperCase());
            switch (stateRate) {
                case LIKE:
                    createdRate.setRate(1);
                    break;
                case DISLIKE:
                    createdRate.setRate(0);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Not correct event State in query");
        }
        return RateMapper.toOutputRateDto(rateRepo.save(createdRate),
                rateRepo.findCountRates(eventId, StateRate.LIKE),
                rateRepo.findCountRates(eventId, StateRate.DISLIKE));
    }

    @Override
    @Transactional
    public void deleteLike(Long userId, Long eventId) {
        UserValidator.checkUserExist(userRepo, userId);
        EventValidator.checkEventExist(eventRepo, eventId);
        RateValidator.checkLikeExists(rateRepo, userId, eventId);
        rateRepo.deleteByUserAndEvent(eventId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PopularEventsDto> getMustPopularEvents(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return rateRepo.findBests(pageable).stream()
                .peek(best -> best.setCountLike(rateRepo.findCountRates(best.getEvent().getId(), StateRate.LIKE)))
                .peek(best -> best.setCountDislike(rateRepo.findCountRates(best.getEvent().getId(), StateRate.DISLIKE)))
                .map(RateMapper::toPopularEventsDto)
                .collect(Collectors.toList());
    }
}
