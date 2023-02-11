package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;

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
    public Item update(Item item) {
        return itemDao.update(item);
    }
}
