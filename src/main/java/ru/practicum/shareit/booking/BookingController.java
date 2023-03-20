package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.State;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.group.Create;

import javax.validation.constraints.Min;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {

    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;

    @PostMapping
    public BookingOutputDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @Validated({Create.class}) @RequestBody BookingInputDto bookingInputDto) {
        UserDto userDto = userService.findById(userId);
        ItemDto itemDto = itemService.getByItemIdAndUserId(bookingInputDto.getItemId(), userId);

        return bookingService.create(userDto, itemDto, bookingInputDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingOutputDto approveByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestParam("approved") Boolean approved,
                                           @PathVariable("bookingId") Long bookingId) {
        return bookingService.approveByOwner(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingOutputDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable("bookingId") Long bookingId) {
        return bookingService.getBookingByIdAndUser(bookingId, userId);
    }

    @GetMapping
    public List<BookingOutputDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestParam(value = "state", defaultValue = "ALL") State state,
                                         @RequestParam(value = "from", required = false, defaultValue = "0") @Min(0) Integer from,
                                         @RequestParam(value = "size", required = false, defaultValue = "10") @Min(1) Integer size) {
        UserDto userDto = userService.findById(userId);

        return bookingService.findAllByBooker(userDto.getId(), state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingOutputDto> getAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestParam(value = "state", defaultValue = "ALL") State state,
                                                @RequestParam(value = "from", required = false, defaultValue = "0") @Min(0) Integer from,
                                                @RequestParam(value = "size", required = false, defaultValue = "10") @Min(1) Integer size) {
        UserDto userDto = userService.findById(userId);

        return bookingService.findAllByOwner(userDto.getId(), state, from, size);
    }
}
