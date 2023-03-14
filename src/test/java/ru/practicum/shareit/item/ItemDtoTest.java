package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.ItemDto;

import java.io.IOException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    private final String jsonToDeserialize = "{\n" +
            "    \"id\": 2,\n" +
            "    \"name\": \"Отвертка\",\n" +
            "    \"description\": \"Аккумуляторная отвертка\",\n" +
            "    \"available\": true,\n" +
            "    \"ownerId\": 4,\n" +
            "    \"lastBooking\": null,\n" +
            "    \"nextBooking\": null,\n" +
            "    \"comments\": [],\n" +
            "    \"requestId\": null\n" +
            "}";

    private ItemDto itemDto = null;

    @BeforeEach
    public void setup() {
        itemDto = new ItemDto(
                2L,
                "Отвертка",
                "Аккумуляторная отвертка",
                true,
                4L,
                null,
                null,
                new ArrayList<>(),
                null);
    }

    @Test
    public void idSerializes() throws IOException {
        assertThat(json.write(itemDto))
                .extractingJsonPathNumberValue("$.id")
                .isEqualTo(2);
    }

    @Test
    public void nameSerializes() throws IOException {
        assertThat(json.write(itemDto))
                .extractingJsonPathStringValue("$.name")
                .isEqualTo("Отвертка");
    }

    @Test
    public void descriptionSerializes() throws IOException {
        assertThat(json.write(itemDto))
                .extractingJsonPathStringValue("$.description")
                .isEqualTo("Аккумуляторная отвертка");
    }

    @Test
    public void availableSerializes() throws IOException {
        assertThat(json.write(itemDto))
                .extractingJsonPathBooleanValue("$.available")
                .isEqualTo(true);
    }

    @Test
    public void ownerIdSerializes() throws IOException {
        assertThat(json.write(itemDto))
                .extractingJsonPathNumberValue("$.ownerId")
                .isEqualTo(4);
    }

    @Test
    public void lastBookingSerializes() throws IOException {
        assertThat(json.write(itemDto))
                .extractingJsonPathMapValue("$.lastBooking")
                .isEqualTo(null);
    }

    @Test
    public void nextBookingSerializes() throws IOException {
        assertThat(json.write(itemDto))
                .extractingJsonPathMapValue("$.nextBooking")
                .isEqualTo(null);
    }

    @Test
    public void commentsSerializes() throws IOException {
        assertThat(json.write(itemDto))
                .extractingJsonPathArrayValue("$.comments").size()
                .isEqualTo(0);
    }

    @Test
    public void requestIdSerializes() throws IOException {
        assertThat(json.write(itemDto))
                .extractingJsonPathArrayValue("$.requestId")
                .isEqualTo(null);
    }

    @Test
    public void nameDeserializes() throws IOException {
        assertThat(json.parseObject(jsonToDeserialize).getName()).isEqualTo("Отвертка");
    }

    @Test
    public void idDeserializes() throws IOException {
        assertThat(json.parseObject(jsonToDeserialize).getId())
                .isEqualTo(2L);
    }

    @Test
    public void emailDeserializes() throws IOException {
        assertThat(json.parseObject(jsonToDeserialize).getDescription())
                .isEqualTo("Аккумуляторная отвертка");
    }

    @Test
    public void availableDeserializes() throws IOException {
        assertThat(json.parseObject(jsonToDeserialize).getAvailable())
                .isEqualTo(true);
    }

    @Test
    public void ownerIdDeserializes() throws IOException {
        assertThat(json.parseObject(jsonToDeserialize).getOwnerId())
                .isEqualTo(4L);
    }

    @Test
    public void lastBookingDeserializes() throws IOException {
        assertThat(json.parseObject(jsonToDeserialize).getLastBooking())
                .isEqualTo(null);
    }

    @Test
    public void nextBookingDeserializes() throws IOException {
        assertThat(json.parseObject(jsonToDeserialize).getNextBooking())
                .isEqualTo(null);
    }

    @Test
    public void commentsDeserializes() throws IOException {
        assertThat(json.parseObject(jsonToDeserialize).getComments().size())
                .isEqualTo(0);
    }

    @Test
    public void requestIdDeserializes() throws IOException {
        assertThat(json.parseObject(jsonToDeserialize).getRequestId())
                .isEqualTo(null);
    }
}