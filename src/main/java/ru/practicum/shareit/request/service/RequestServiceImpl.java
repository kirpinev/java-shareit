package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    private final ItemService itemService;

    @Override
    @Transactional
    public RequestDto create(RequestDto requestDto, UserDto userDto) {
        Request request = RequestMapper.toRequest(requestDto, userDto);

        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getAllOwnRequestsById(Long userId) {
        List<Request> requests = requestRepository.findAllByRequestorIdOrderByCreatedAsc(userId);

        return requests.stream().map(request -> {
            List<ItemDto> items = itemService.getAllByRequestId(request.getId());

            return RequestMapper.toRequestDto(request, items);
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RequestDto getByRequestId(Long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> {
            throw new NotFoundException(String.format("Запроса с id %s нет", requestId));
        });

        return RequestMapper.toRequestDto(request, itemService.getAllByRequestId(request.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getAllRequests(Long userId, Integer from, Integer size) {
        List<Request> requests = requestRepository
                .findAllByRequestorIdNotOrderByCreatedAsc(userId, PageRequest.of(from, size));

        return requests
                .stream()
                .map(request -> RequestMapper.toRequestDto(request,
                        itemService.getAllByRequestId(request.getId())))
                .collect(Collectors.toList());
    }
}
