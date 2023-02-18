package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto create(ItemDto itemDto, UserDto userDto) {
        Item item = itemMapper.toItem(itemDto);
        User user = new User(userDto.getId(), userDto.getName(), userDto.getEmail());
        item.setOwner(user);

        return itemMapper.toItemDto(itemDao.create(item));
    }

    @Override
    public Item findByUserIdAndItemId(Long userId, Long itemId) {
        return itemDao.findByUserIdAndItemId(userId, itemId).orElseThrow(() -> {
            throw new NotFoundException(String.format("Вещи с id %s нет", itemId));
        });
    }

    @Override
    public ItemDto findById(Long itemId) {
        Item item = itemDao.findById(itemId).orElseThrow(() -> {
            throw new NotFoundException(String.format("Вещи с id %s нет", itemId));
        });

        return itemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> findAllByUserId(Long userId) {
        return itemDao.findAllByUserId(userId)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findAllByText(String text) {
        return itemDao.findByText(text)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        Item item = findByUserIdAndItemId(userId, itemId);
        Item itemFromDto = itemMapper.toItem(itemDto);
        itemFromDto.setId(item.getId());
        itemFromDto.setOwner(item.getOwner());

        if (Objects.nonNull(itemFromDto.getName())
                && !itemFromDto.getName().isEmpty()
                && !itemFromDto.getName().isBlank()) {
            item.setName(itemFromDto.getName());
        }

        if (Objects.nonNull(itemFromDto.getDescription())
                && !itemFromDto.getDescription().isEmpty()
                && !itemFromDto.getDescription().isBlank()) {
            item.setDescription(itemFromDto.getDescription());
        }

        if (Objects.nonNull(itemFromDto.getAvailable())) {
            item.setAvailable(itemFromDto.getAvailable());
        }

        return itemMapper.toItemDto(itemDao.update(item, itemId));
    }
}
