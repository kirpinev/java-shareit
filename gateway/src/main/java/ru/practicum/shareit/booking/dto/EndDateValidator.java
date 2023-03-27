package ru.practicum.shareit.booking.dto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class EndDateValidator implements ConstraintValidator<ValidEndDate, BookingInputDto> {
    @Override
    public boolean isValid(BookingInputDto value, ConstraintValidatorContext context) {
        return value.getStart().isBefore(value.getEnd());
    }
}