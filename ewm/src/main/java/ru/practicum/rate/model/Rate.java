package ru.practicum.rate.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "rates")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rate {
    @EmbeddedId
    private RatePK ratePK;
    @Column(name = "rate")
    private Integer rate;
}
