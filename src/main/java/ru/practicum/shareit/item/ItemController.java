package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final UserService userService;
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @Valid @RequestBody ItemDto itemDto) {
        User user = userService.getEntityById(userId);
        Item itemFromDto = itemMapper.toItem(itemDto);

        itemFromDto.setOwner(user);

        Item createdItem = itemService.create(itemFromDto);

        return itemMapper.toItemDto(createdItem);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        userService.getEntityById(userId);

        return itemService
                .getAllByUserId(userId)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @PathVariable("itemId") Long itemId) {
        userService.getEntityById(userId);

        Item item = itemService.getEntityById(itemId);

        return itemMapper.toItemDto(item);
    }

    @GetMapping("/search")
    public List<ItemDto> getByText(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @RequestParam(name = "text") String text) {
        userService.getEntityById(userId);

        List<Item> items = itemService.getEntitiesByText(text);

        return items
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @RequestBody ItemDto itemDto,
                          @PathVariable("itemId") Long itemId) {
        User user = userService.getEntityById(userId);
        Item item = itemService.getEntityByUserIdAndItemId(userId, itemId);
        Item itemFromDto = itemMapper.toItem(itemDto);

        itemFromDto.setId(item.getId());
        itemFromDto.setOwner(user);

        Item updatedItem = itemService.updateById(itemFromDto, itemId);

        return itemMapper.toItemDto(updatedItem);
    }
}
