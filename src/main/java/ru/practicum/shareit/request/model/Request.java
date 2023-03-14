package ru.practicum.shareit.request.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.shareit.item.model.Item;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "requests")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "requestor_id", nullable = false)
    private Long requestorId;
    @Column(name = "created")
    @CreationTimestamp
    private LocalDateTime created;
    @OneToMany
    @JoinColumn(name = "request_id")
    @ToString.Exclude
    private List<Item> items = new ArrayList<>();
}
