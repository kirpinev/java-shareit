package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotFoundException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingServiceIntegrationTest {

    @Autowired
    BookingService bookingService;

    @Test
    void approveByOwnerWrongBookingId() {
        Long id = 2L;

        final NotFoundException exception = Assertions
                .assertThrows(NotFoundException.class, () -> bookingService.approveByOwner(id, id, true));

        Assertions.assertEquals(String.format("Бронирования с id %s нет", id), exception.getMessage());
    }

    @Test
    void getBookingByIdAndUserWrongBookingId() {
        Long id = 2L;

        final NotFoundException exception = Assertions
                .assertThrows(NotFoundException.class, () -> bookingService.getBookingByIdAndUser(id, id));

        Assertions.assertEquals(String.format("Бронирования с id %s нет", id), exception.getMessage());
    }
}
