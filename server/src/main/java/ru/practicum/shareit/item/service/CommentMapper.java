package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {

    private CommentMapper() {
    }

    public static Comment toComment(CommentDto commentDto, UserDto userDto, ItemDto itemDto) {
        User user = new User();
        Item item = new Item();

        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(user.getEmail());

        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());

        return new Comment(commentDto.getId(), commentDto.getText(),
                item, user, commentDto.getCreated());
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getAuthor().getName(),
                comment.getCreated());
    }

    public static List<CommentDto> toCommentDto(List<Comment> comments) {
        return comments
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
