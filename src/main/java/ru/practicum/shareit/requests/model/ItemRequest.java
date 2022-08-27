package ru.practicum.shareit.requests.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.model.ItemShort;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "requests")
@Validated
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_description")
    private String description;

    @OneToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    @Transient
    private List<ItemShort> items;

    private LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRequest that = (ItemRequest) o;
        return Objects.equals(description, that.description) && Objects.equals(requester, that.requester)
                && Objects.equals(items, that.items) && Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, requester, items, created);
    }
}
