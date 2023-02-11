package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    public ItemDto toItemDto(Item item) {
        return new ItemDto()
                .toBuilder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable()).build();
    }

    public Item toItem(ItemDto itemDto) {
        return new Item()
                .toBuilder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .id(itemDto.getId())
                .build();
    }
}
