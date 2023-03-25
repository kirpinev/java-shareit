package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RequestRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RequestRepository requestRepository;

    private final User user = new User(
            null,
            "Igor",
            "igor@gmail.dom");

    private final User anotherUser = new User(
            null,
            "Stas",
            "stas@gmail.dom");

    private final Request request = new Request(
            null,
            "Запрос",
            1L,
            LocalDateTime.now(),
            new ArrayList<>());

    private final Request anotherRequest = new Request(
            null,
            "Другой запрос",
            2L,
            LocalDateTime.now().plusDays(1),
            new ArrayList<>());

    @BeforeEach
    void setup() {
        entityManager.persist(user);
        entityManager.persist(anotherUser);
        entityManager.flush();
    }

    @Test
    void createRequest() {
        Request createdRequest = requestRepository.save(request);

        Assertions.assertNotNull(createdRequest);
        Assertions.assertEquals(1L, createdRequest.getId());
        Assertions.assertEquals(request.getDescription(), createdRequest.getDescription());
        Assertions.assertEquals(request.getRequestorId(), createdRequest.getRequestorId());
        Assertions.assertEquals(request.getCreated(), createdRequest.getCreated());
    }

    @Test
    void findRequestById() {
        entityManager.persist(request);
        entityManager.flush();

        Request found = requestRepository.findById(1L).orElse(null);

        Assertions.assertNotNull(found);
        Assertions.assertEquals(1L, found.getId());
        Assertions.assertEquals(request.getDescription(), found.getDescription());
        Assertions.assertEquals(request.getRequestorId(), found.getRequestorId());
        Assertions.assertEquals(request.getCreated(), found.getCreated());
    }

    @Test
    void findAllByRequestorIdNotOrderByCreatedAsc() {
        entityManager.persist(request);
        entityManager.persist(anotherRequest);
        entityManager.flush();

        Long user2Id = 2L;
        Long user1Id = 1L;
        List<Request> requests = requestRepository
                .findAllByRequestorIdNotOrderByCreatedAsc(user2Id, PageRequest.of(0, 1));

        Assertions.assertEquals(1, requests.size());
        Assertions.assertEquals(1L, requests.get(0).getId());
        Assertions.assertEquals(request.getDescription(), requests.get(0).getDescription());
        Assertions.assertEquals(request.getCreated(), requests.get(0).getCreated());
        Assertions.assertEquals(user1Id, requests.get(0).getRequestorId());
    }

    @Test
    void findAllByRequestorIdOrderByCreatedAsc() {
        entityManager.persist(request);
        entityManager.persist(anotherRequest);
        entityManager.flush();

        Long user2Id = 2L;
        List<Request> requests = requestRepository.findAllByRequestorIdOrderByCreatedAsc(user2Id);

        Assertions.assertEquals(1, requests.size());
        Assertions.assertEquals(2L, requests.get(0).getId());
        Assertions.assertEquals(anotherRequest.getDescription(), requests.get(0).getDescription());
        Assertions.assertEquals(anotherRequest.getCreated(), requests.get(0).getCreated());
        Assertions.assertEquals(user2Id, requests.get(0).getRequestorId());
    }
}
