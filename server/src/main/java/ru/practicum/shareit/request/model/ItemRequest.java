package ru.practicum.shareit.request.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.model.ItemShort;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "requests")
@Validated
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class ItemRequest {

    @Id
    @EqualsAndHashCode.Exclude
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
}
