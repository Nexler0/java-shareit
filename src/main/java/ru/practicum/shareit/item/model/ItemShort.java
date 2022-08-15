package ru.practicum.shareit.item.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ItemShort {
    @Id
    private Long id;
    private String name;
    private String description;

}
