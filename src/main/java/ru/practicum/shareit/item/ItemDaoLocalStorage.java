package ru.practicum.shareit.item;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemDaoLocalStorage implements ItemDao {
    private final Map<Long, List<Item>> itemsMap = new HashMap<>();
    private Long itemId = 1L;

    @Override
    public Item create(Item item) {
        item.setId(itemId);
        itemId += 1;

        itemsMap.compute(item.getOwner().getId(), (userId, userItems) -> {
            if (Objects.isNull(userItems)) {
                userItems = new ArrayList<>();
            }

            userItems.add(item);

            return userItems;
        });

        return item;
    }

    @Override
    public Optional<Item> getEntityByUserIdAndItemId(Long userId, Long itemId) {
        List<Item> items = itemsMap.get(userId);

        if (Objects.nonNull(items)) {
            return items
                    .stream()
                    .filter(item -> Objects.equals(item.getId(), itemId))
                    .findFirst();
        }

        return Optional.empty();
    }

    public Optional<Item> getEntityById(Long itemId) {
        List<Item> items = new ArrayList<>();
        itemsMap.values().forEach(items::addAll);

        return items
                .stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst();
    }

    @Override
    public List<Item> getAllByUserId(Long userId) {
        return itemsMap.get(userId);
    }

    @Override
    public List<Item> getEntitiesByText(String text) {
        List<Item> items = new ArrayList<>();
        itemsMap.values().forEach(items::addAll);

        if (text.length() > 0) {
            return items
                    .stream()
                    .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                            || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                    .filter(Item::getAvailable)
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    @Override
    public Item update(Item item) {
        List<Item> items = itemsMap.get(item.getOwner().getId());
        Optional<Item> itemFromList = items
                .stream()
                .filter(item1 -> Objects.equals(item1.getId(), item.getId()))
                .findFirst();

        itemFromList.ifPresent((item1 -> {
            if (Objects.nonNull(item.getName())
                    && !item.getName().isEmpty() && !item.getName().isBlank()) {
                item1.setName(item.getName());
            }

            if (Objects.nonNull(item.getDescription())
                    && !item.getDescription().isEmpty() && !item.getDescription().isBlank()) {
                item1.setDescription(item.getDescription());
            }

            if (Objects.nonNull(item.getAvailable())) {
                item1.setAvailable(item.getAvailable());
            }
        }));

        return itemFromList.orElse(null);
    }
}
