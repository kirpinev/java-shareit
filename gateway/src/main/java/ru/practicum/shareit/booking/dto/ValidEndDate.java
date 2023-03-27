package ru.practicum.shareit.booking.dto;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EndDateValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@interface ValidEndDate {

    String message() default "Дата окончания не может быть раньше даты начала";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
