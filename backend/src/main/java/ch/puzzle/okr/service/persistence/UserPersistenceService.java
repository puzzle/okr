package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserPersistenceService extends PersistenceBase<User, Long> {
    protected UserPersistenceService(UserRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return "User";
    }

    public synchronized User getOrCreateUser(User newUser) {
        Optional<User> user = ((UserRepository) repository).findByUsername(newUser.getUsername());
        return user.orElseGet(() -> repository.save(newUser));
    }
}
