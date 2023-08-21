package ru.practicum.validator;

import ru.practicum.events.dto.InputEventDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class CreateEventStartValidator implements ConstraintValidator<EventStartBefore, InputEventDto> {
    EventStartBefore check;

    @Override
    public void initialize(EventStartBefore check) {
        this.check = check;
    }

    @Override
    public boolean isValid(InputEventDto inputEventDto, ConstraintValidatorContext ctx) {
        return (inputEventDto.getEventDate() != null &&
                inputEventDto.getEventDate().isAfter(LocalDateTime.now().plusHours(check.min())));
    }
}
