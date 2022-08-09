package ru.practicum.shareit.item.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "items")
@Validated
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(table = "users", name = "id")
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "items_name")
    private String name;

    @Column(name = "items_description")
    private String description;

    @Column(name = "items_available")
    private Boolean available;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id && userId == item.userId && available == item.available && name.equals(item.name) && description.equals(item.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, name, description, available);
    }
}
