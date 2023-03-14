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

    private final LocalDateTime time = LocalDateTime.parse("2023-03-15T14:38:28");

    private final String jsonToDeserialize = "{\n" +
            "\"id\": 2,\n" +
            "\"start\": \"2023-03-15T14:38:28\",\n" +
            "\"end\": \"2023-03-15T14:38:28\",\n" +
            "\"status\": \"WAITING\",\n" +
            "\"user\": null,\n" +
            "\"item\": null\n" +
            "}";

    private BookingOutputDto bookingOutputDto = null;

    @BeforeEach
    public void setup() {
        bookingOutputDto = new BookingOutputDto(
                2L,
                time,
                time,
                Status.WAITING.name(),
                null,
                null);
    }

    @Test
    public void idSerializes() throws IOException {
        assertThat(json.write(bookingOutputDto))
                .extractingJsonPathNumberValue("$.id")
                .isEqualTo(2);
    }

    @Test
    public void startSerializes() throws IOException {
        assertThat(json.write(bookingOutputDto))
                .extractingJsonPathStringValue("$.start")
                .isEqualTo("2023-03-15T14:38:28");
    }

    @Test
    public void endSerializes() throws IOException {
        assertThat(json.write(bookingOutputDto))
                .extractingJsonPathStringValue("$.end")
                .isEqualTo("2023-03-15T14:38:28");
    }

    @Test
    public void bookerSerializes() throws IOException {
        assertThat(json.write(bookingOutputDto))
                .extractingJsonPathMapValue("$.booker")
                .isEqualTo(null);
    }

    @Test
    public void itemSerializes() throws IOException {
        assertThat(json.write(bookingOutputDto))
                .extractingJsonPathMapValue("$.item")
                .isEqualTo(null);
    }

    @Test
    public void idDeserializes() throws IOException {
        assertThat(json.parseObject(jsonToDeserialize).getId())
                .isEqualTo(2L);
    }

    @Test
    public void startDeserializes() throws IOException {
        assertThat(json.parseObject(jsonToDeserialize).getStart())
                .isEqualTo("2023-03-15T14:38:28");
    }

    @Test
    public void endDeserializes() throws IOException {
        assertThat(json.parseObject(jsonToDeserialize).getEnd())
                .isEqualTo("2023-03-15T14:38:28");
    }

    @Test
    public void bookerDeserializes() throws IOException {
        assertThat(json.parseObject(jsonToDeserialize).getBooker())
                .isEqualTo(null);
    }

    @Test
    public void itemDeserializes() throws IOException {
        assertThat(json.parseObject(jsonToDeserialize).getItem())
                .isEqualTo(null);
    }
}
