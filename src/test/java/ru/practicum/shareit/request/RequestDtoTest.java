package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.RequestDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class RequestDtoTest {

    @Autowired
    private JacksonTester<RequestDto> json;

    private final ObjectMapper mapper = new ObjectMapper();

    ItemDto itemDto = new ItemDto(
            4L,
            "Щётка для обуви",
            "Стандартная щётка для обуви",
            true,
            4L,
            null,
            null,
            Collections.emptyList(),
            1L);

    private final RequestDto requestDto = new RequestDto(
            1L,
            "Хотел бы воспользоваться щёткой для обуви",
            LocalDateTime.now(),
            List.of(itemDto));

    @Test
    void testSerialize() throws Exception {
        JsonContent<RequestDto> result = json.write(requestDto);

        assertThat(result).hasJsonPathNumberValue("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).hasJsonPathStringValue("$.description");
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo(requestDto.getDescription());
        assertThat(result).hasJsonPathStringValue("$.created");
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo(requestDto.getCreated().truncatedTo(ChronoUnit.SECONDS).toString());
        assertThat(result).hasJsonPathArrayValue("$.items");
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id")
                .isEqualTo(requestDto.getItems().get(0).getId().intValue());
    }

    @Test
    void testDeserialize() throws Exception {
        mapper.findAndRegisterModules();

        String jsonContent = mapper.writeValueAsString(requestDto);
        RequestDto result = json.parse(jsonContent).getObject();

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getDescription()).isEqualTo("Хотел бы воспользоваться щёткой для обуви");
        assertThat(result.getCreated()).isEqualTo(requestDto.getCreated().truncatedTo(ChronoUnit.SECONDS));
        assertThat(result.getItems().size()).isEqualTo(1);
        assertThat(result.getItems().get(0)).isEqualTo(itemDto);
    }
}
