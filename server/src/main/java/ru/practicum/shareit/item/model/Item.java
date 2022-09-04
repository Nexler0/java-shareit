package ru.practicum.shareit.item.model;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.model.BookingShort;
import ru.practicum.shareit.comment.model.CommentShort;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "items")
@Validated
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class Item {

    @Id
    @EqualsAndHashCode.Exclude
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

    @Transient
    private BookingShort lastBooking;

    @Transient
    private BookingShort nextBooking;

    @Transient
    private List<CommentShort> comments;
}
