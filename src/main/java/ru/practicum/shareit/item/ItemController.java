package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.group.Create;
import ru.practicum.shareit.validation.group.Update;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final UserService userService;
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @Validated({Create.class}) @RequestBody ItemDto itemDto) {
        UserDto userDto = userService.findById(userId);

        return itemService.create(itemDto, userDto);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                @RequestParam(value = "from", required = false, defaultValue = "0") @Min(0) Integer from,
                                @RequestParam(value = "size", required = false, defaultValue = "10") @Min(1) Integer size) {
        userService.findById(userId);

        return itemService.getAllByUserId(userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@Validated({Create.class}) @RequestBody CommentDto commentDto,
                                 @RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long itemId) {
        UserDto userDto = userService.findById(userId);
        ItemDto itemDto = itemService.getByItemIdAndUserId(itemId, userId);

        return itemService.createComment(commentDto, userDto, itemDto, LocalDateTime.now());
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @PathVariable("itemId") Long itemId) {
        userService.findById(userId);

        return itemService.getByItemIdAndUserId(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getByText(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @RequestParam(name = "text") String text,
                                   @RequestParam(value = "from", required = false, defaultValue = "0") @Min(0) Integer from,
                                   @RequestParam(value = "size", required = false, defaultValue = "10") @Min(1) Integer size) {
        userService.findById(userId);

        return itemService.getAllByText(text, from, size);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @Validated({Update.class}) @RequestBody ItemDto itemDto,
                          @PathVariable("itemId") Long itemId) {
        UserDto userDto = userService.findById(userId);

        return itemService.update(userDto, itemId, itemDto);
    }
}
