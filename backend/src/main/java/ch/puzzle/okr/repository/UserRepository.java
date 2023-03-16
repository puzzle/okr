package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(@Param("username") String username);
}
