package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
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
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        userService.findById(userId);

        return itemService.getAllByUserId(userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@Validated({Create.class}) @RequestBody CommentDto commentDto,
                                 @RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long itemId) {
        UserDto userDto = userService.findById(userId);
        ItemDto itemDto = itemService.getByItemIdAndUserId(itemId, userId);

        return itemService.createComment(commentDto, userDto, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @PathVariable("itemId") Long itemId) {
        userService.findById(userId);

        return itemService.getByItemIdAndUserId(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getByText(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @RequestParam(name = "text") String text) {
        userService.findById(userId);

        return itemService.getAllByText(text);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @Validated({Update.class}) @RequestBody ItemDto itemDto,
                          @PathVariable("itemId") Long itemId) {
        UserDto userDto = userService.findById(userId);

        return itemService.update(userDto, itemId, itemDto);
    }
}
