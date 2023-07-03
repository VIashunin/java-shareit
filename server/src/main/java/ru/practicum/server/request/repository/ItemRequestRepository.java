package ru.practicum.server.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.server.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findItemRequestByRequestor_Id(long userId);

    List<ItemRequest> findItemRequestByIdNotOrderByCreatedDesc(long userId, Pageable page);
}
