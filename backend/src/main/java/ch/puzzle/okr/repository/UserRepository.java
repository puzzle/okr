package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
