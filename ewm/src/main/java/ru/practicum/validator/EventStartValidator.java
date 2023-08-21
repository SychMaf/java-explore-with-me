package ru.practicum.validator;

import ru.practicum.events.dto.UpdateUserEventDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class EventStartValidator implements ConstraintValidator<EventStartBefore, UpdateUserEventDto> {
    EventStartBefore check;

    @Override
    public void initialize(EventStartBefore check) {
        this.check = check;
    }

    @Override
    public boolean isValid(UpdateUserEventDto updateUserEventDto, ConstraintValidatorContext ctx) {
        if(updateUserEventDto.getEventDate() != null) {
            return (updateUserEventDto.getEventDate().isAfter(LocalDateTime.now().plusHours(check.min())));
        } else {
            return true;
        }
    }
}
