package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.Status;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingOutputDtoTest {

    @Autowired
    private JacksonTester<BookingOutputDto> json;

    private static final String DATE_TIME = "2023-03-15T14:38:28";

    private BookingOutputDto bookingOutputDto = null;

    @BeforeEach
    public void setup() {
        bookingOutputDto = new BookingOutputDto(
                2L,
                LocalDateTime.parse("2023-03-15T14:38:28.100"),
                LocalDateTime.parse("2023-03-15T14:38:28.100"),
                Status.WAITING.name(),
                null,
                null);
    }

    @Test
    public void startSerializes() throws IOException {
        assertThat(json.write(bookingOutputDto))
                .extractingJsonPathStringValue("$.start")
                .isEqualTo(DATE_TIME);
    }

    @Test
    public void endSerializes() throws IOException {
        assertThat(json.write(bookingOutputDto))
                .extractingJsonPathStringValue("$.end")
                .isEqualTo(DATE_TIME);
    }
}
