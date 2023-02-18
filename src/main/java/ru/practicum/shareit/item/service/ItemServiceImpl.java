package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;

    @Override
    public Item create(Item item) {
        return itemDao.create(item);
    }

    @Override
    public Item getEntityByUserIdAndItemId(Long userId, Long itemId) {
        return itemDao.getEntityByUserIdAndItemId(userId, itemId).orElseThrow(() -> {
            throw new NotFoundException(String.format("Вещи с id %s нет", itemId));
        });
    }

    @Override
    public Item getEntityById(Long itemId) {
        return itemDao.getEntityById(itemId).orElseThrow(() -> {
            throw new NotFoundException(String.format("Вещи с id %s нет", itemId));
        });
    }

    @Override
    public List<Item> getAllByUserId(Long userId) {
        return itemDao.getAllByUserId(userId);
    }

    @Override
    public List<Item> getEntitiesByText(String text) {
        return itemDao.getEntitiesByText(text);
    }

    @Override
    public Item updateById(Item item, Long itemId) {
        Optional<Item> itemFromMap = itemDao.getEntityById(itemId);

        if (itemFromMap.isPresent()) {
            if (Objects.nonNull(item.getName())
                    && !item.getName().isEmpty() && !item.getName().isBlank()) {
                itemFromMap.get().setName(item.getName());
            }

            if (Objects.nonNull(item.getDescription())
                    && !item.getDescription().isEmpty() && !item.getDescription().isBlank()) {
                itemFromMap.get().setDescription(item.getDescription());
            }

            if (Objects.nonNull(item.getAvailable())) {
                itemFromMap.get().setAvailable(item.getAvailable());
            }

            return itemDao.updateById(itemFromMap.get(), itemId);
        }

        return null;
    }
}
