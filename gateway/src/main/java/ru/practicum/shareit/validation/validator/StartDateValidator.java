package ru.practicum.shareit.validation.validator;

import ru.practicum.shareit.booking.dto.BookingInputDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class StartDateValidator implements ConstraintValidator<ValidStartDate, BookingInputDto> {
    @Override
    public boolean isValid(BookingInputDto value, ConstraintValidatorContext context) {
        return !value.getStart().isEqual(value.getEnd());
    }
}