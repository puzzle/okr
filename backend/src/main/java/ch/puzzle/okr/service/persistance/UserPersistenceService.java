package ch.puzzle.okr.service.persistance;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserPersistenceService extends PersistenceBase<User, Long> {
    protected UserPersistenceService(UserRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return "User";
    }
}
