package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.practicum.shareit.user.model.User;

@RepositoryRestResource(path = "users")
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    @Query("select u from User u where upper(u.email) like upper(concat('%', ?1, '%'))")
    User findByEmailContainingIgnoreCase(String email);

    @Query("select (count(u) > 0) from User u where u.id = ?1")
    Boolean existsUserById(Long id);

    User getUserById(Long id);

    @Modifying
    @Query("update User u set u.name = ?1, u.email = ?2 where u.id=?3")
    void setUserInfoById(String name, String email, Long id);

    @Query("select u from User u where u.email = ?1")
    User getByEmailEquals(String email);
}
