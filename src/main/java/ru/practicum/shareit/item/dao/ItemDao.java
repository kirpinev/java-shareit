package ru.practicum.shareit.item.dao;


import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDao {
    Item create(Item item);

    Optional<Item> getEntityByUserIdAndItemId(Long userId, Long itemId);

    Optional<Item> getEntityById(Long itemId);

    List<Item> getAllByUserId(Long userId);

    List<Item> getEntitiesByText(String text);

    Item updateById(Item item, Long itemId);
}
