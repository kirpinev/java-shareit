package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingInputDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingInputDtoTest {

    @Autowired
    private JacksonTester<BookingInputDto> json;

    private final ObjectMapper mapper = new ObjectMapper();

    private final BookingInputDto bookingInputDto = new BookingInputDto(
            1L,
            LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
            LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS));

    @Test
    void testSerialize() throws Exception {
        JsonContent<BookingInputDto> result = json.write(bookingInputDto);

        assertThat(result).hasJsonPathNumberValue("$.itemId");
        assertThat(result).extractingJsonPathNumberValue("$.itemId")
                .isEqualTo(bookingInputDto.getItemId().intValue());
        assertThat(result).hasJsonPathStringValue("$.start");
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(bookingInputDto.getStart().toString());
        assertThat(result).hasJsonPathStringValue("$.end");
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo(bookingInputDto.getEnd().toString());
    }

    @Test
    public void testDeserialize() throws Exception {
        mapper.findAndRegisterModules();

        String jsonContent = mapper.writeValueAsString(bookingInputDto);
        BookingInputDto result = json.parse(jsonContent).getObject();

        assertThat(result.getItemId()).isEqualTo(bookingInputDto.getItemId());
        assertThat(result.getStart()).isEqualTo(bookingInputDto.getStart());
        assertThat(result.getEnd()).isEqualTo(bookingInputDto.getEnd());
    }
}
