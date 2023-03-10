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
import java.util.List;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
            LocalDateTime.now());

    private final Request anotherRequest = new Request(
            null,
            "Другой запрос",
            2L,
            LocalDateTime.now().plusDays(1));

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
        Assertions.assertEquals(createdRequest.getId(), 1L);
        Assertions.assertEquals(createdRequest.getDescription(), request.getDescription());
        Assertions.assertEquals(createdRequest.getRequestorId(), request.getRequestorId());
        Assertions.assertEquals(createdRequest.getCreated(), request.getCreated());
    }

    @Test
    void findRequestById() {
        entityManager.persist(request);
        entityManager.flush();

        Request found = requestRepository.findById(1L).orElse(null);

        Assertions.assertNotNull(found);
        Assertions.assertEquals(found.getId(), 1L);
        Assertions.assertEquals(found.getDescription(), request.getDescription());
        Assertions.assertEquals(found.getRequestorId(), request.getRequestorId());
        Assertions.assertEquals(found.getCreated(), request.getCreated());
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

        Assertions.assertEquals(requests.size(), 1);
        Assertions.assertEquals(requests.get(0).getId(), 1L);
        Assertions.assertEquals(requests.get(0).getDescription(), request.getDescription());
        Assertions.assertEquals(requests.get(0).getCreated(), request.getCreated());
        Assertions.assertEquals(requests.get(0).getRequestorId(), user1Id);
    }

    @Test
    void findAllByRequestorIdOrderByCreatedAsc() {
        entityManager.persist(request);
        entityManager.persist(anotherRequest);
        entityManager.flush();

        Long user2Id = 2L;
        List<Request> requests = requestRepository.findAllByRequestorIdOrderByCreatedAsc(user2Id);

        Assertions.assertEquals(requests.size(), 1);
        Assertions.assertEquals(requests.get(0).getId(), 2L);
        Assertions.assertEquals(requests.get(0).getDescription(), anotherRequest.getDescription());
        Assertions.assertEquals(requests.get(0).getCreated(), anotherRequest.getCreated());
        Assertions.assertEquals(requests.get(0).getRequestorId(), user2Id);
    }
}
