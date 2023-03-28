package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface RequestService {
    RequestDto create(RequestDto requestDto, UserDto userDto);

    RequestDto getByRequestId(Long requestId);

    List<RequestDto> getAllOwnRequestsById(Long userId);

    List<RequestDto> getAllRequests(Long userId, Integer from, Integer size);
}
