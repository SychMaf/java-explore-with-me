package ru.practicum.validator.annotation;

import javax.validation.groups.Default;

public interface ValidationGroups {
    interface Create extends Default {
    }

    interface Update extends Default {
    }
}
