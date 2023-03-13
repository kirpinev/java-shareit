package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingOutputDtoTest {

    @Autowired
    private JacksonTester<BookingOutputDto> json;

    private final ObjectMapper mapper = new ObjectMapper();

    private final UserDto userDto = new UserDto(
            1L,
            "Igor",
            "igor@gmail.dom");

    private final ItemDto itemDto = new ItemDto(
            1L,
            "Какая-то вещь",
            "Какое-то описание",
            true,
            1L,
            null,
            null,
            new ArrayList<>(),
            1L);

    private final BookingOutputDto bookingOutputDto = new BookingOutputDto(
            1L,
            LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
            LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS),
            Status.WAITING,
            userDto,
            itemDto);

    @Test
    void testSerialize() throws Exception {
        JsonContent<BookingOutputDto> result = json.write(bookingOutputDto);

        assertThat(result).hasJsonPathNumberValue("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(bookingOutputDto.getId().intValue());
        assertThat(result).hasJsonPathStringValue("$.start");
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(bookingOutputDto.getStart().toString());
        assertThat(result).hasJsonPathStringValue("$.end");
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo(bookingOutputDto.getEnd().toString());
        assertThat(result).hasJsonPathStringValue("$.status");
        assertThat(result).extractingJsonPathStringValue("$.status")
                .isEqualTo(bookingOutputDto.getStatus().toString());
        assertThat(result).hasJsonPathNumberValue("$.booker.id");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id")
                .isEqualTo(bookingOutputDto.getBooker().getId().intValue());
        assertThat(result).hasJsonPathNumberValue("$.item.id");
        assertThat(result).extractingJsonPathNumberValue("$.item.id")
                .isEqualTo(bookingOutputDto.getItem().getId().intValue());
    }

    @Test
    public void testDeserialize() throws Exception {
        mapper.findAndRegisterModules();

        String jsonContent = mapper.writeValueAsString(bookingOutputDto);
        BookingOutputDto result = json.parse(jsonContent).getObject();

        assertThat(result.getId()).isEqualTo(bookingOutputDto.getId());
        assertThat(result.getStart()).isEqualTo(bookingOutputDto.getStart());
        assertThat(result.getEnd()).isEqualTo(bookingOutputDto.getEnd());
        assertThat(result.getBooker().getId()).isEqualTo(bookingOutputDto.getBooker().getId());
        assertThat(result.getItem().getId()).isEqualTo(bookingOutputDto.getItem().getId());
    }
}
