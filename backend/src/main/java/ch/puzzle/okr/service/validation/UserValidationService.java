package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.service.persistance.UserPersistenceService;
import org.springframework.stereotype.Service;

@Service
public class UserValidationService extends ValidationBase<User, Long> {

    UserValidationService(UserPersistenceService persistenceService) {
        super(persistenceService);
    }

    @Override
    public void validateOnCreate(User user) {
        isModelNull(user);
        throwExceptionWhenIdIsNotNull(user.getId());
        validate(user);
    }

    @Override
    public void validateOnUpdate(Long id, User user) {
        isModelNull(user);
        throwExceptionWhenIdIsNull(user.getId());

        validate(user);
    }
}
