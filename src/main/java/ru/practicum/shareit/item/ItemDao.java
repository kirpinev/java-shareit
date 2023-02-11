package ru.practicum.shareit.item;


import java.util.List;
import java.util.Optional;

public interface ItemDao {
    Item create(Item item);

    Optional<Item> getEntityByUserIdAndItemId(Long userId, Long itemId);

    Optional<Item> getEntityById(Long itemId);

    List<Item> getAllByUserId(Long userId);

    List<Item> getEntitiesByText(String text);

    Item update(Item item);
}
