package ru.practicum.shareit.validation.validator;

import ru.practicum.shareit.booking.dto.BookingInputDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class EndDateValidator implements ConstraintValidator<ValidEndDate, BookingInputDto> {
    @Override
    public boolean isValid(BookingInputDto value, ConstraintValidatorContext context) {
        return value.getStart().isBefore(value.getEnd());
    }
}