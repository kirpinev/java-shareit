package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
            time,
            new ArrayList<>());

    @Test
    void createRequest() {
        when(requestRepository.save(any(Request.class))).thenReturn(request);

        RequestDto createdRequest = requestService.create(requestDto, userDto);

        Assertions.assertNotNull(createdRequest);
        Assertions.assertEquals(requestDto.getId(), createdRequest.getId());
        Assertions.assertEquals(requestDto.getDescription(), createdRequest.getDescription());
        Assertions.assertEquals(requestDto.getCreated(), createdRequest.getCreated());
        Assertions.assertEquals(requestDto.getItems().size(), createdRequest.getItems().size());

        verify(requestRepository, times(1)).save(any(Request.class));
        verifyNoMoreInteractions(requestRepository);
    }

    @Test
    void getAllOwnRequestsById() {
        when(requestRepository.findAllByRequestorIdOrderByCreatedAsc(anyLong()))
                .thenReturn(List.of(request));

        List<RequestDto> requests = requestService.getAllOwnRequestsById(anyLong());

        Assertions.assertEquals(1, requests.size());
        Assertions.assertEquals(requestDto.getId(), requests.get(0).getId());
        Assertions.assertEquals(requestDto.getDescription(), requests.get(0).getDescription());
        Assertions.assertEquals(requestDto.getCreated(), requests.get(0).getCreated());
        Assertions.assertEquals(requestDto.getItems().size(), requests.get(0).getItems().size());

        verify(requestRepository, times(1))
                .findAllByRequestorIdOrderByCreatedAsc(anyLong());
        verifyNoMoreInteractions(requestRepository);
    }

    @Test
    void getByRequestId() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request));

        RequestDto foundRequest = requestService.getByRequestId(anyLong());

        Assertions.assertNotNull(foundRequest);
        Assertions.assertEquals(requestDto.getId(), foundRequest.getId());
        Assertions.assertEquals(requestDto.getDescription(), foundRequest.getDescription());
        Assertions.assertEquals(requestDto.getCreated(), foundRequest.getCreated());
        Assertions.assertEquals(requestDto.getItems().size(), foundRequest.getItems().size());

        verify(requestRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(requestRepository);
    }

    @Test
    void getAllRequests() {
        Pageable pageable = PageRequest.of(0, 1);

        when(requestRepository.findAllByRequestorIdNotOrderByCreatedAsc(1L, pageable))
                .thenReturn(List.of(request));

        List<RequestDto> requests = requestService.getAllRequests(1L, 0, 1);

        Assertions.assertEquals(1, requests.size());
        Assertions.assertEquals(requestDto.getId(), requests.get(0).getId());
        Assertions.assertEquals(requestDto.getDescription(), requests.get(0).getDescription());
        Assertions.assertEquals(requestDto.getCreated(), requests.get(0).getCreated());
        Assertions.assertEquals(requestDto.getItems().size(), requests.get(0).getItems().size());

        verify(requestRepository, times(1))
                .findAllByRequestorIdNotOrderByCreatedAsc(anyLong(), any(Pageable.class));
        verifyNoMoreInteractions(requestRepository);
    }
}
