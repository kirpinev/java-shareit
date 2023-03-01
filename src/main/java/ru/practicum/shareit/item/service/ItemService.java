package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto itemDto, UserDto userDto);

    ItemDto getByItemIdAndUserId(Long itemId, Long userId);

    List<ItemDto> getAllByUserId(Long userId);

    List<ItemDto> getAllByText(String text);

    ItemDto update(UserDto userDto, Long itemId, ItemDto item);

    CommentDto createComment(CommentDto commentDto, UserDto userDto, ItemDto itemDto);

    List<CommentDto> getAllCommentsByItemId(Long itemId);
}
