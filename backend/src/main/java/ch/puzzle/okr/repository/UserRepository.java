package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends DeleteRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findByOkrChampion(boolean isOkrChampion);
}
