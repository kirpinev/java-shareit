package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.dto.BookingInputDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingInputDtoTest {

    @Autowired
    private JacksonTester<BookingInputDto> json;

    private final LocalDateTime time = LocalDateTime.parse("2023-03-15T14:38:28");

    private final String jsonToDeserialize = "{\n" +
            "\"itemId\": 2,\n" +
            "\"start\": \"2023-03-15T14:38:28\",\n" +
            "\"end\": \"2023-03-15T14:38:28\"\n" +
            "}";

    private BookingInputDto bookingInputDto = null;

    @BeforeEach
    public void setup() {
        bookingInputDto = new BookingInputDto(
                2L,
                time,
                time);
    }

    @Test
    public void itemIdSerializes() throws IOException {
        assertThat(json.write(bookingInputDto))
                .extractingJsonPathNumberValue("$.itemId")
                .isEqualTo(2);
    }

    @Test
    public void startSerializes() throws IOException {
        assertThat(json.write(bookingInputDto))
                .extractingJsonPathStringValue("$.start")
                .isEqualTo("2023-03-15T14:38:28");
    }

    @Test
    public void endSerializes() throws IOException {
        assertThat(json.write(bookingInputDto))
                .extractingJsonPathStringValue("$.end")
                .isEqualTo("2023-03-15T14:38:28");
    }

    @Test
    public void itemIdDeserializes() throws IOException {
        assertThat(json.parseObject(jsonToDeserialize).getItemId())
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
}
