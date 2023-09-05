package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static ch.puzzle.okr.SpringCachingConfig.USER_CACHE;

@Service
public class UserPersistenceService extends PersistenceBase<User, Long> {
    protected UserPersistenceService(UserRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return "User";
    }

    @Cacheable(value = USER_CACHE, key = "#newUser.username")
    public synchronized User getOrCreateUser(User newUser) {
        Optional<User> user = getUserRepository().findByUsername(newUser.getUsername());
        return user.orElseGet(() -> repository.save(newUser));
    }

    @Cacheable(value = USER_CACHE, key = "#username")
    public User findUserByUsername(String username) {
        return getUserRepository().findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        (String.format("User with username %s not found", username))));
    }

    private UserRepository getUserRepository() {
        return (UserRepository) repository;
    }
}
