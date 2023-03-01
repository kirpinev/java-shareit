package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.dto.BookerInfoDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
            return ItemMapper.toItemDto(item, getLastBooking(item),
                    getNextBooking(item), getAllCommentsByItemId(itemId));
        }

        return ItemMapper.toItemDto(item, getAllCommentsByItemId(itemId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getAllByUserId(Long userId) {
        return itemRepository.findAllByOwnerIdOrderByIdAsc(userId)
                .stream()
                .map(item -> ItemMapper.toItemDto(item, getLastBooking(item),
                        getNextBooking(item), getAllCommentsByItemId(item.getId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getAllByText(String text) {
        if (text.length() == 0) {
            return Collections.emptyList();
        }

        return itemRepository.search(text)
                .stream()
                .map(item -> ItemMapper.toItemDto(item, getLastBooking(item),
                        getNextBooking(item), getAllCommentsByItemId(item.getId())))
                .collect(Collectors.toList());
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

        return ItemMapper.toItemDto(itemRepository.save(item), getLastBooking(item),
                getNextBooking(item), getAllCommentsByItemId(item.getId()));
    }

    @Override
    @Transactional
    public CommentDto createComment(CommentDto commentDto, UserDto userDto, ItemDto itemDto) {
        Comment comment = CommentMapper.toComment(commentDto, userDto, itemDto);
        List<Booking> bookings = bookingRepository
                .getAllUserBookings(userDto.getId(), itemDto.getId(), LocalDateTime.now());

        if (bookings.isEmpty()) {
            throw new BadRequestException("Чтобы оставить комментарий нужно сначала оформить бронирование");
        }

        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getAllCommentsByItemId(Long itemId) {
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        return comments
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    private BookerInfoDto getLastBooking(Item item) {
        return bookingRepository
                .getLastBooking(item.getId(), LocalDateTime.now())
                .map(BookingMapper::toBookingInfoDto)
                .orElse(null);
    }

    private BookerInfoDto getNextBooking(Item item) {
        return bookingRepository
                .getNextBooking(item.getId(), LocalDateTime.now())
                .map(BookingMapper::toBookingInfoDto)
                .orElse(null);
    }
}
