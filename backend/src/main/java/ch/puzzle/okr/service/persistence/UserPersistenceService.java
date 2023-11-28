package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static ch.puzzle.okr.Constants.USER;
import static ch.puzzle.okr.SpringCachingConfig.USER_CACHE;

@Service
public class UserPersistenceService extends PersistenceBase<User, Long, UserRepository> {
    protected UserPersistenceService(UserRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return USER;
    }

    @Cacheable(value = USER_CACHE, key = "#user.username")
    public synchronized User getOrCreateUser(User user) {
        Optional<User> savedUser = getRepository().findByUsername(user.getUsername());
        return savedUser.orElseGet(() -> getRepository().save(user));
    }
}
