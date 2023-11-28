package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.repository.UserRepository;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class UserValidationService extends ValidationBase<User, Long, UserRepository, UserPersistenceService> {

    private static final Logger logger = LoggerFactory.getLogger(UserValidationService.class);

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

    public void validateAuthorisationToken(Jwt token) {
        if (token == null) {
            logger.warn("invalid token (null)");
            throw new OkrResponseStatusException(BAD_REQUEST, ErrorKey.TOKEN_NULL);
        }
    }
}
