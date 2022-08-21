package ru.practicum.shareit.requests;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

@RepositoryRestResource(path = "requests")
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long>, ItemRequestRepositoryCustom {

    @Override
    @Query("select i from ItemRequest i order by i.created desc")
    @NonNull
    Page<ItemRequest> findAll (@NonNull Pageable pageable);

    @Query("select i from ItemRequest i where i.id = ?1")
    ItemRequest getItemRequestById(Long requestId);

    @Query("select i from ItemRequest i where i.requester.id = ?1")
    List<ItemRequest> findItemRequestByRequesterId(Long userId);
}
