package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.State;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private UserService userService;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

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
            Status.WAITING.name(),
            userDto,
            itemDto);

    private final BookingInputDto bookingInputDto = new BookingInputDto(
            1L,
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(1));

    @BeforeEach
    void setup() {
        when(userService.findById(anyLong())).thenReturn(userDto);
    }

    @Test
    void createNewBooking() throws Exception {
        when(userService.findById(anyLong())).thenReturn(userDto);
        when(itemService.getByItemIdAndUserId(anyLong(), anyLong())).thenReturn(itemDto);
        when(bookingService.create(any(UserDto.class), any(ItemDto.class), any(BookingInputDto.class)))
                .thenReturn(bookingOutputDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingOutputDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingOutputDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingOutputDto.getEnd().toString())))
                .andExpect(jsonPath("$.status", is(bookingOutputDto.getStatus())))
                .andExpect(jsonPath("$.booker.id", is(bookingOutputDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingOutputDto.getItem().getId()), Long.class));
    }

    @Test
    void approvedByOwner() throws Exception {
        when(bookingService.approveByOwner(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingOutputDto);

        mvc.perform(patch("/bookings/{bookingId}", "1")
                        .content(mapper.writeValueAsString(bookingInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingOutputDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingOutputDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingOutputDto.getEnd().toString())))
                .andExpect(jsonPath("$.status", is(bookingOutputDto.getStatus())))
                .andExpect(jsonPath("$.booker.id", is(bookingOutputDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingOutputDto.getItem().getId()), Long.class));
    }

    @Test
    void getById() throws Exception {
        when(bookingService.getBookingByIdAndUser(anyLong(), anyLong())).thenReturn(bookingOutputDto);

        mvc.perform(get("/bookings/{bookingId}", "1")
                        .content(mapper.writeValueAsString(bookingInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingOutputDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingOutputDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingOutputDto.getEnd().toString())))
                .andExpect(jsonPath("$.status", is(bookingOutputDto.getStatus())))
                .andExpect(jsonPath("$.booker.id", is(bookingOutputDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingOutputDto.getItem().getId()), Long.class));
    }

    @Test
    void getAll() throws Exception {
        when(userService.findById(anyLong())).thenReturn(userDto);
        when(bookingService.findAllByBooker(anyLong(), any(State.class), anyInt(), anyInt()))
                .thenReturn(List.of(bookingOutputDto));

        mvc.perform(get("/bookings")
                        .content(mapper.writeValueAsString(bookingInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingOutputDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(bookingOutputDto.getStart().toString())))
                .andExpect(jsonPath("$[0].end", is(bookingOutputDto.getEnd().toString())))
                .andExpect(jsonPath("$[0].status", is(bookingOutputDto.getStatus())))
                .andExpect(jsonPath("$[0].booker.id", is(bookingOutputDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(bookingOutputDto.getItem().getId()), Long.class));
    }

    @Test
    void getAllByOwner() throws Exception {
        when(userService.findById(anyLong())).thenReturn(userDto);
        when(bookingService.findAllByOwner(anyLong(), any(State.class), anyInt(), anyInt()))
                .thenReturn(List.of(bookingOutputDto));

        mvc.perform(get("/bookings/owner")
                        .content(mapper.writeValueAsString(bookingInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingOutputDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(bookingOutputDto.getStart().toString())))
                .andExpect(jsonPath("$[0].end", is(bookingOutputDto.getEnd().toString())))
                .andExpect(jsonPath("$[0].status", is(bookingOutputDto.getStatus())))
                .andExpect(jsonPath("$[0].booker.id", is(bookingOutputDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(bookingOutputDto.getItem().getId()), Long.class));
    }
}
