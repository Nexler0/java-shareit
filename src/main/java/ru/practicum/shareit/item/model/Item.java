package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
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
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "items_name")
    private String name;

    @Column(name = "items_description")
    private String description;

    @Column(name = "items_available")
    private Boolean available;

    @OneToOne
    @MapsId
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
