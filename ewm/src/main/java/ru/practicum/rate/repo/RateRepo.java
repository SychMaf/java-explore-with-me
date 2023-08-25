package ru.practicum.rate.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.rate.model.PopularEvents;
import ru.practicum.rate.model.Rate;
import ru.practicum.rate.model.RatePK;
import ru.practicum.rate.model.StateRate;

import java.util.List;

public interface RateRepo extends JpaRepository<Rate, RatePK> {
    String agrFunction = "trunc((((sum(r.rate)) / (count(r.ratePK.user))) + ln(count(r.ratePK.user)))*100)";
    default Boolean existsByUserAndEvent(Long eventId, Long userId) {
        return existsByRatePK_Event_IdAndRatePK_User_Id(eventId, userId);
    }

    default Long findCountRates(Long eventId, StateRate rate) {
        switch (rate) {
            case LIKE:
                return countByRatePK_Event_IdAndRate(eventId, 1);
            case DISLIKE:
                return countByRatePK_Event_IdAndRate(eventId, 0);
        }
        return null;
    }

    default void deleteByUserAndEvent(Long eventId, Long userId) {
        deleteByRatePK_Event_IdAndRatePK_User_Id(eventId, userId);
    }

    List<Rate> findAllByRatePK_User_Id(Long userId);

    Boolean existsByRatePK_Event_IdAndRatePK_User_Id(Long eventId, Long userId);

    void deleteByRatePK_Event_IdAndRatePK_User_Id(Long eventId, Long userId);

    Long countByRatePK_Event_IdAndRate(Long eventId, Integer rate);

    @Query(value = "select new ru.practicum.rate.model.PopularEvents(r.ratePK.event, " + agrFunction + ", 0L, 0L) from Rate r " +
            "group by r.ratePK.event " +
            "order by " + agrFunction + " desc")
    Page<PopularEvents> findBests(Pageable pageable);
}
