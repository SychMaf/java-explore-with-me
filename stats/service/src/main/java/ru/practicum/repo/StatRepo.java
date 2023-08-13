package ru.practicum.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.OutputDto;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepo extends JpaRepository<Stats, Long> {

    @Query(value = "select new ru.practicum.dto.OutputDto(s.app, s.uri , count(s.ip)) from Stats s" +
            " where s.requestTime > ?1 " +
            "and s.requestTime < ?2 " +
            "group by s.uri, s.app " +
            "order by count(s.ip) desc")
    List<OutputDto> findAllWithoutUrisAndNotUniqueIp(LocalDateTime before, LocalDateTime after);

    @Query(value = "select new ru.practicum.dto.OutputDto(s.app, s.uri , COUNT(s.ip)) from Stats s" +
            " where s.requestTime > ?1 " +
            "and s.requestTime < ?2 " +
            "and s.uri in ?3 " +
            "group by s.uri, s.app " +
            "order by count(s.ip) desc")
    List<OutputDto> findAllWithUrisAndNotUniqueIp(LocalDateTime before, LocalDateTime after, List<String> uris);

    @Query(value = "select new ru.practicum.dto.OutputDto(s.app, s.uri , COUNT(distinct s.ip)) from Stats s" +
            " where s.requestTime > ?1 " +
            "and s.requestTime < ?2 " +
            "group by s.uri, s.app " +
            "order by count(s.ip) desc")
    List<OutputDto> findWithoutUrisAndUniqueIp(LocalDateTime before, LocalDateTime after);

    @Query(value = "select new ru.practicum.dto.OutputDto(s.app, s.uri , COUNT(distinct s.ip)) from Stats s" +
            " where s.requestTime > ?1 " +
            "and s.requestTime < ?2 " +
            "and s.uri in ?3 " +
            "group by s.uri, s.app " +
            "order by count(s.ip) desc")
    List<OutputDto> findWithUrisAndUniqueIp(LocalDateTime before, LocalDateTime after, List<String> uris);
}
