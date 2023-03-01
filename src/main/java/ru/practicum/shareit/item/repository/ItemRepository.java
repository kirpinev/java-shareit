package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerIdOrderByIdAsc(Long ownerId);

    Item findByOwnerIdAndId(Long ownerId, Long id);

    @Query("select i from Item i " +
            "where (lower(i.name) like '%'||lower(:text)||'%' " +
            "or lower(i.description) like '%'||lower(:text)||'%') and i.available = true")
    List<Item> search(@Param("text") String text);
}