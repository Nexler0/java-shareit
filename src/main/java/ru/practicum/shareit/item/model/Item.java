package ru.practicum.shareit.item.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "items")
@Validated
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "items_name")
    @NotNull
    private String name;

    @Column(name = "items_description")
    @NotNull
    private String description;

    @Column(name = "items_available")
    @NotNull
    private Boolean available;

    @OneToOne
    @JoinColumn(name = "request_id")
    private ItemRequest itemRequest;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id)
                && Objects.equals(getUser().getId(), item.getUser().getId())
                && available == item.available
                && name.equals(item.name)
                && description.equals(item.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getUser().getId(), name, description, available);
    }
}
