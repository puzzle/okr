package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.repository.UserRepository;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import org.springframework.stereotype.Service;

@Service
public class UserValidationService extends ValidationBase<User, Long, UserRepository, UserPersistenceService> {

    UserValidationService(UserPersistenceService persistenceService) {
        super(persistenceService);
    }

    @Override
    public void validateOnCreate(User model) {
        throwExceptionWhenModelIsNull(model);
        throwExceptionWhenIdIsNotNull(model.getId());
        validate(model);
    }

    public void validateOnGetOrCreate(User model) {
        throwExceptionWhenModelIsNull(model);
        validate(model);
    }

    @Override
    public void validateOnUpdate(Long id, User model) {
        throwExceptionWhenModelIsNull(model);
        throwExceptionWhenIdIsNull(model.getId());
        throwExceptionWhenIdHasChanged(id, model.getId());
        validate(model);
    }
}
