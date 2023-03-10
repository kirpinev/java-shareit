package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.BookerInfoDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    private final ObjectMapper mapper = new ObjectMapper();

    private final CommentDto commentDto = new CommentDto(
            1L,
            "Какой-то текст",
            "Igor",
            LocalDateTime.now());
    private final BookerInfoDto bookerInfoDto = new BookerInfoDto(
            1L,
            1L,
            LocalDateTime.now(),
            LocalDateTime.now());
    private final ItemDto itemDto = new ItemDto(
            1L,
            "Какая-то вещь",
            "Какое-то описание",
            true,
            1L,
            bookerInfoDto,
            bookerInfoDto,
            List.of(commentDto),
            1L);

    @Test
    void testSerialize() throws Exception {
        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).hasJsonPathStringValue("$.name");
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemDto.getName());
        assertThat(result).hasJsonPathStringValue("$.description");
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemDto.getDescription());
        assertThat(result).hasJsonPathBooleanValue("$.available");
        assertThat(result).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(itemDto.getAvailable());
        assertThat(result).hasJsonPathNumberValue("$.ownerId");
        assertThat(result).extractingJsonPathNumberValue("$.ownerId")
                .isEqualTo(itemDto.getOwnerId().intValue());
        assertThat(result).hasJsonPathMapValue("$.lastBooking");
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.id")
                .isEqualTo(itemDto.getLastBooking().getId().intValue());
        assertThat(result).hasJsonPathMapValue("$.nextBooking");
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.id")
                .isEqualTo(itemDto.getNextBooking().getId().intValue());
        assertThat(result).hasJsonPathArrayValue("$.comments");
        assertThat(result).extractingJsonPathNumberValue("$.comments[0].id")
                .isEqualTo(itemDto.getComments().get(0).getId().intValue());
    }

    @Test
    public void testDeserialize() throws Exception {
        mapper.findAndRegisterModules();

        String jsonContent = mapper.writeValueAsString(itemDto);
        ItemDto result = json.parse(jsonContent).getObject();

        assertThat(result.getId()).isEqualTo(itemDto.getId());
        assertThat(result.getName()).isEqualTo(itemDto.getName());
        assertThat(result.getDescription()).isEqualTo(itemDto.getDescription());
        assertThat(result.getAvailable()).isTrue();
        assertThat(result.getOwnerId()).isEqualTo(itemDto.getOwnerId());
        assertThat(result.getLastBooking().getId()).isEqualTo(itemDto.getLastBooking().getId());
        assertThat(result.getNextBooking().getId()).isEqualTo(itemDto.getNextBooking().getId());
        assertThat(result.getComments().size()).isEqualTo(itemDto.getComments().size());
        assertThat(result.getComments().get(0).getId()).isEqualTo(itemDto.getComments().get(0).getId());
        assertThat(result.getRequestId()).isEqualTo(itemDto.getRequestId());
    }
}