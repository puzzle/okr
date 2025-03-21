package ch.puzzle.okr.service.persistence;

import static ch.puzzle.okr.Constants.USER;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserPersistenceService extends PersistenceBase<User, Long, UserRepository> {
    protected UserPersistenceService(UserRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return USER;
    }

    public synchronized User getOrCreateUser(User user) {
        Optional<User> savedUser = getRepository().findByEmail(user.getEmail());
        return savedUser.orElseGet(() -> getRepository().save(user));
    }

    public Optional<User> findByEmail(String email) {
        return getRepository().findByEmail(email);
    }

    @Override
    public User save(User user) {
        if (user.getUserTeamList() == null) {
            user.setUserTeamList(List.of());
        }
        return getRepository().save(user);
    }

    public List<User> findAllOkrChampions() {
        return getRepository().findByOkrChampion(true);
    }

    public Iterable<User> saveAll(List<User> userList) {
        return getRepository().saveAll(userList);
    }
}
