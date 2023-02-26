package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Component
public class CommentMapper {

    public Comment toComment(CommentDto commentDto, UserDto userDto, ItemDto itemDto) {
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

    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getAuthor().getName(),
                comment.getCreated());
    }
}
