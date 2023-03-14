package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.BookerInfoDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingInfoDtoTest {

    @Autowired
    private JacksonTester<BookerInfoDto> json;

    private final LocalDateTime time = LocalDateTime.parse("2023-03-15T14:38:28");

    private final String jsonToDeserialize = "{\n" +
            "\"id\": 4,\n" +
            "\"bookerId\": 5,\n" +
            "\"start\": \"2023-03-15T14:38:28\",\n" +
            "\"end\": \"2023-03-15T14:38:28\"\n" +
            "}";

    private BookerInfoDto bookerInfoDto = null;

    @BeforeEach
    public void setup() {
        bookerInfoDto = new BookerInfoDto(
                4L,
                5L,
                time,
                time
        );
    }

    @Test
    public void idSerializes() throws IOException {
        assertThat(json.write(bookerInfoDto))
                .extractingJsonPathNumberValue("$.id")
                .isEqualTo(4);
    }

    @Test
    public void bookerIdSerializes() throws IOException {
        assertThat(json.write(bookerInfoDto))
                .extractingJsonPathNumberValue("$.bookerId")
                .isEqualTo(5);
    }

    @Test
    public void startSerializes() throws IOException {
        assertThat(json.write(bookerInfoDto))
                .extractingJsonPathStringValue("$.start")
                .isEqualTo("2023-03-15T14:38:28");
    }

    @Test
    public void endSerializes() throws IOException {
        assertThat(json.write(bookerInfoDto))
                .extractingJsonPathStringValue("$.end")
                .isEqualTo("2023-03-15T14:38:28");
    }


    @Test
    public void idDeserializes() throws IOException {
        assertThat(this.json.parseObject(jsonToDeserialize).getId())
                .isEqualTo(4L);
    }

    @Test
    public void bookerIdDeserializes() throws IOException {
        assertThat(this.json.parseObject(jsonToDeserialize).getBookerId())
                .isEqualTo(5L);
    }

    @Test
    public void startDeserializes() throws IOException {
        assertThat(this.json.parseObject(jsonToDeserialize).getStart())
                .isEqualTo("2023-03-15T14:38:28");
    }

    @Test
    public void endDeserializes() throws IOException {
        assertThat(this.json.parseObject(jsonToDeserialize).getEnd())
                .isEqualTo("2023-03-15T14:38:28");
    }
}
