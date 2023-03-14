package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.request.dto.RequestDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class RequestDtoTest {

    @Autowired
    private JacksonTester<RequestDto> json;

    private final LocalDateTime time = LocalDateTime.parse("2023-03-15T14:38:28");

    private final String jsonToDeserialize = "{\n" +
            "    \"id\": 1,\n" +
            "    \"description\": \"Хотел бы воспользоваться щёткой для обуви\",\n" +
            "    \"created\": \"2023-03-15T14:38:28\",\n" +
            "    \"items\": []\n" +
            "}";

    private RequestDto requestDto = null;

    @BeforeEach
    public void setup() {
        requestDto = new RequestDto(
                1L,
                "Хотел бы воспользоваться щёткой для обуви",
                time,
                new ArrayList<>());
    }

    @Test
    public void idSerializes() throws IOException {
        assertThat(json.write(requestDto))
                .extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
    }

    @Test
    public void descriptionSerializes() throws IOException {
        assertThat(json.write(requestDto))
                .extractingJsonPathStringValue("$.description")
                .isEqualTo("Хотел бы воспользоваться щёткой для обуви");
    }

    @Test
    public void createdSerializes() throws IOException {
        assertThat(json.write(requestDto))
                .extractingJsonPathStringValue("$.created")
                .isEqualTo("2023-03-15T14:38:28");
    }

    @Test
    public void itemsSerializes() throws IOException {
        assertThat(json.write(requestDto))
                .extractingJsonPathArrayValue("$.items").size()
                .isEqualTo(0);
    }

    @Test
    public void idDeserializes() throws IOException {
        assertThat(json.parseObject(jsonToDeserialize).getId())
                .isEqualTo(1);
    }

    @Test
    public void descriptionDeserializes() throws IOException {
        assertThat(json.parseObject(jsonToDeserialize).getDescription())
                .isEqualTo("Хотел бы воспользоваться щёткой для обуви");
    }

    @Test
    public void createdDeserializes() throws IOException {
        assertThat(json.parseObject(jsonToDeserialize).getCreated())
                .isEqualTo("2023-03-15T14:38:28");
    }

    @Test
    public void itemsDeserializes() throws IOException {
        assertThat(json.parseObject(jsonToDeserialize).getItems().size())
                .isEqualTo(0);
    }
}
