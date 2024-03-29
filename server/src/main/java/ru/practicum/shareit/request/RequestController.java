package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class RequestController {

    private final RequestService requestService;
    private final UserService userService;

    @PostMapping
    public RequestDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @RequestBody RequestDto requestDto) {
        UserDto userDto = userService.findById(userId);

        return requestService.create(requestDto, userDto);
    }

    @GetMapping
    public List<RequestDto> getAllOwnRequestsById(@RequestHeader("X-Sharer-User-Id") Long userId) {
        UserDto userDto = userService.findById(userId);

        return requestService.getAllOwnRequestsById(userDto.getId());
    }

    @GetMapping("/{requestId}")
    public RequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable("requestId") Long requestId) {
        userService.findById(userId);

        return requestService.getByRequestId(requestId);
    }

    @GetMapping("/all")
    public List<RequestDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                           @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        UserDto userDto = userService.findById(userId);

        return requestService.getAllRequests(userDto.getId(), from, size);
    }
}
