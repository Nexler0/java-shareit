package ru.practicum.shareit.user.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "users", schema = "public")
@Validated
@NoArgsConstructor
@Getter @Setter @ToString
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "users_name")
    private String name;

    @Column(name = "users_email")
    @Email
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equals(user.name) && email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }
}
