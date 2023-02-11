package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    Item create(Item item);

    Item getEntityByUserIdAndItemId(Long userId, Long itemId);

    Item getEntityById(Long itemId);

    List<Item> getAllByUserId(Long userId);

    List<Item> getEntitiesByText(String text);

    Item update(Item item);
}
