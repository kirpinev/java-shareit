package ru.practicum.shareit.request.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

public class RequestMapper {

    private RequestMapper() {
    }

    public static Request toRequest(RequestDto requestDto, UserDto userDto) {
        Request request = new Request();

        request.setId(requestDto.getId());
        request.setDescription(requestDto.getDescription());
        request.setRequestorId(userDto.getId());
        request.setCreated(requestDto.getCreated());

        return request;
    }

    public static RequestDto toRequestDto(Request request, List<ItemDto> items) {
        return new RequestDto(request.getId(), request.getDescription(), request.getCreated(), items);
    }

    public static RequestDto toRequestDto(Request request) {
        return new RequestDto(request.getId(), request.getDescription(), request.getCreated(), new ArrayList<>());
    }
}
