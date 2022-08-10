package ru.practicum.shareit.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;

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

    @Column(name = "requestor_id")
    private Long requestorId;
}
