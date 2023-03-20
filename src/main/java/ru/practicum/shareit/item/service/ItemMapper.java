package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.BookerInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class ItemMapper {

    private ItemMapper() {
    }

    public static ItemDto toItemDto(Item item, BookerInfoDto lastBooking,
                                    BookerInfoDto nextBooking) {
        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner().getId(),
                lastBooking,
                nextBooking,
                CommentMapper.toCommentDto(item.getComments()),
                item.getRequestId());
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner().getId(),
                null,
                null,
                CommentMapper.toCommentDto(item.getComments()),
                item.getRequestId());
    }

    public static Item toItem(ItemDto itemDto, UserDto userDto) {
        Item item = new Item();
        User user = new User();

        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        item.setId(itemDto.getId());
        item.setDescription(itemDto.getDescription());
        item.setName(itemDto.getName());
        item.setAvailable(itemDto.getAvailable());
        item.setRequestId(itemDto.getRequestId());
        item.setOwner(user);

        return item;
    }
}
