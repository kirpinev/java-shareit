package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestServiceImplTest {

    @InjectMocks
    private RequestServiceImpl requestService;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private ItemService itemService;

    private final UserDto userDto = new UserDto(
            1L,
            "Igor",
            "igor@gmail.dom");

    private final LocalDateTime time = LocalDateTime.now();

    private final RequestDto requestDto = new RequestDto(
            1L,
            "Какой-то запрос",
            time,
            new ArrayList<>());

    private final Request request = new Request(
            1L,
            "Какой-то запрос",
            1L,
            time);

    @Test
    void createRequest() {
        when(requestRepository.save(any(Request.class))).thenReturn(request);

        RequestDto createdRequest = requestService.create(requestDto, userDto);

        Assertions.assertNotNull(createdRequest);
        Assertions.assertEquals(createdRequest.getId(), requestDto.getId());
        Assertions.assertEquals(createdRequest.getDescription(), requestDto.getDescription());
        Assertions.assertEquals(createdRequest.getCreated(), requestDto.getCreated());
        Assertions.assertEquals(createdRequest.getItems().size(), requestDto.getItems().size());

        verify(requestRepository, times(1)).save(any(Request.class));
        verifyNoMoreInteractions(requestRepository);
    }

    @Test
    void getAllOwnRequestsById() {
        when(requestRepository.findAllByRequestorIdOrderByCreatedAsc(anyLong()))
                .thenReturn(List.of(request));
        when(itemService.getAllByRequestId(anyLong())).thenReturn(Collections.emptyList());

        List<RequestDto> requests = requestService.getAllOwnRequestsById(anyLong());

        Assertions.assertEquals(requests.size(), 1);
        Assertions.assertEquals(requests.get(0).getId(), requestDto.getId());
        Assertions.assertEquals(requests.get(0).getDescription(), requestDto.getDescription());
        Assertions.assertEquals(requests.get(0).getCreated(), requestDto.getCreated());
        Assertions.assertEquals(requests.get(0).getItems().size(), requestDto.getItems().size());

        verify(requestRepository, times(1))
                .findAllByRequestorIdOrderByCreatedAsc(anyLong());
        verifyNoMoreInteractions(requestRepository);
        verify(itemService, times(1)).getAllByRequestId(anyLong());
        verifyNoMoreInteractions(itemService);
    }

    @Test
    void getByRequestId() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request));
        when(itemService.getAllByRequestId(anyLong())).thenReturn(Collections.emptyList());

        RequestDto foundRequest = requestService.getByRequestId(anyLong());

        Assertions.assertNotNull(foundRequest);
        Assertions.assertEquals(foundRequest.getId(), requestDto.getId());
        Assertions.assertEquals(foundRequest.getDescription(), requestDto.getDescription());
        Assertions.assertEquals(foundRequest.getCreated(), requestDto.getCreated());
        Assertions.assertEquals(foundRequest.getItems().size(), requestDto.getItems().size());

        verify(requestRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(requestRepository);
        verify(itemService, times(1)).getAllByRequestId(anyLong());
        verifyNoMoreInteractions(itemService);
    }

    @Test
    void getAllRequests() {
        Pageable pageable = PageRequest.of(0, 1);

        when(requestRepository.findAllByRequestorIdNotOrderByCreatedAsc(1L, pageable))
                .thenReturn(List.of(request));
        when(itemService.getAllByRequestId(anyLong())).thenReturn(Collections.emptyList());

        List<RequestDto> requests = requestService.getAllRequests(1L, 0, 1);

        Assertions.assertEquals(requests.size(), 1);
        Assertions.assertEquals(requests.get(0).getId(), requestDto.getId());
        Assertions.assertEquals(requests.get(0).getDescription(), requestDto.getDescription());
        Assertions.assertEquals(requests.get(0).getCreated(), requestDto.getCreated());
        Assertions.assertEquals(requests.get(0).getItems().size(), requestDto.getItems().size());

        verify(requestRepository, times(1))
                .findAllByRequestorIdNotOrderByCreatedAsc(anyLong(), any(Pageable.class));
        verifyNoMoreInteractions(requestRepository);
        verify(itemService, times(1)).getAllByRequestId(anyLong());
        verifyNoMoreInteractions(itemService);
    }
}
