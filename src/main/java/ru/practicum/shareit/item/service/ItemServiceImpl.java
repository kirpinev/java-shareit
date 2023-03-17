package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.BookerInfoDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private static final String ITEM_NOT_FOUND_MESSAGE = "Вещи с id %s нет";

    @Override
    @Transactional
    public ItemDto create(ItemDto itemDto, UserDto userDto) {
        Item item = ItemMapper.toItem(itemDto, userDto);

        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getByItemIdAndUserId(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> {
            throw new NotFoundException(String.format(ITEM_NOT_FOUND_MESSAGE, itemId));
        });

        if (item.getOwner().getId().equals(userId)) {
            return ItemMapper.toItemDto(itemRepository.save(item),
                    getBooking(item, getLastBookings(List.of(item.getId()))),
                    getBooking(item, getNextBookings(List.of(item.getId()))));
        }

        return ItemMapper.toItemDto(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getAllByUserId(Long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Item> items = itemRepository.findAllByOwnerIdOrderByIdAsc(userId, pageable);

        return getItemDtos(items);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getAllByRequestId(Long requestId) {
        List<Item> items = itemRepository.findAllByRequestIdOrderByIdAsc(requestId);

        return getItemDtos(items);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getAllByText(String text, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);

        if (text.length() == 0) {
            return Collections.emptyList();
        }

        List<Item> items = itemRepository.search(text, pageable);

        return getItemDtos(items);
    }

    @Override
    @Transactional
    public ItemDto update(UserDto userDto, Long itemId, ItemDto itemDto) {
        Item item = itemRepository.findByOwnerIdAndId(userDto.getId(), itemId);

        if (item == null) {
            throw new NotFoundException(String.format(ITEM_NOT_FOUND_MESSAGE, itemId));
        }

        Item itemFromDto = ItemMapper.toItem(itemDto, userDto);

        item.setName(Objects.requireNonNullElse(itemFromDto.getName(), item.getName()));
        item.setDescription(Objects.requireNonNullElse(itemFromDto.getDescription(), item.getDescription()));
        item.setAvailable(Objects.requireNonNullElse(itemFromDto.getAvailable(), item.getAvailable()));

        return ItemMapper.toItemDto(itemRepository.save(item),
                getBooking(item, getLastBookings(List.of(item.getId()))),
                getBooking(item, getNextBookings(List.of(item.getId()))));
    }

    @Override
    @Transactional
    public CommentDto createComment(CommentDto commentDto,
                                    UserDto userDto, ItemDto itemDto, LocalDateTime time) {
        Comment comment = CommentMapper.toComment(commentDto, userDto, itemDto);
        List<Booking> bookings = bookingRepository
                .getAllUserBookings(userDto.getId(), itemDto.getId(), time);

        if (bookings.isEmpty()) {
            throw new BadRequestException("Чтобы оставить комментарий нужно сначала оформить бронирование");
        }

        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    private Map<Long, List<Booking>> getLastBookings(List<Long> itemIds) {
        return bookingRepository
                .getLastBooking(itemIds, LocalDateTime.now())
                .stream()
                .collect(Collectors.groupingBy(booking -> booking.getItem().getId()));
    }

    private Map<Long, List<Booking>> getNextBookings(List<Long> itemIds) {
        return bookingRepository
                .getNextBooking(itemIds, LocalDateTime.now())
                .stream()
                .collect(Collectors.groupingBy(booking -> booking.getItem().getId()));
    }

    private BookerInfoDto getBooking(Item item, Map<Long, List<Booking>> bookingsMap) {
        Optional<Booking> booking = Optional
                .ofNullable(bookingsMap.get(item.getId()))
                .orElseGet(Collections::emptyList)
                .stream()
                .findFirst();

        return booking.map(BookingMapper::toBookingInfoDto).orElse(null);
    }

    private List<ItemDto> getItemDtos(List<Item> items) {
        List<Long> itemIds = getItemsIds(items);
        Map<Long, List<Booking>> lastBookings = getLastBookings(itemIds);
        Map<Long, List<Booking>> nextBookings = getNextBookings(itemIds);

        return items
                .stream()
                .map(item -> ItemMapper.toItemDto(item, getBooking(item, lastBookings),
                        getBooking(item, nextBookings)))
                .collect(Collectors.toList());
    }

    private List<Long> getItemsIds(List<Item> items) {
        return items
                .stream()
                .map(Item::getId)
                .collect(Collectors.toList());
    }
}
