package ru.practicum.shareit.booking.dto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class StartDateValidator implements ConstraintValidator<ValidStartDate, BookingInputDto> {
    @Override
    public boolean isValid(BookingInputDto value, ConstraintValidatorContext context) {
        return !value.getStart().isEqual(value.getEnd());
    }
}