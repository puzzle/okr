package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends DeleteRepository<User, Long> {
    Optional<User> findByEmailAndIsDeletedFalse(String email);

    List<User> findByOkrChampionAndIsDeletedFalse(boolean isOkrChampion);
}
