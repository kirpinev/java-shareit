package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(Item item);

    Item getEntityByUserIdAndItemId(Long userId, Long itemId);

    Item getEntityById(Long itemId);

    List<Item> getAllByUserId(Long userId);

    List<Item> getEntitiesByText(String text);

    Item updateById(Item item, Long itemId);
}
