package ch.puzzle.okr.repository;

import java.util.List;
import java.util.Optional;

import ch.puzzle.okr.models.User;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findByIsOkrChampion(boolean okrChampion);
}
