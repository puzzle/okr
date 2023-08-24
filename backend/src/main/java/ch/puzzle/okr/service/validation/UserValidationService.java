package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserValidationService extends ValidationBase<User, Long> {

    UserValidationService(UserPersistenceService persistenceService) {
        super(persistenceService);
    }

    @Override
    public void validateOnCreate(User user) {
        throwExceptionIfModelIsNull(user);
        throwExceptionWhenIdIsNotNull(user.getId());
        validate(user);
    }

    @Override
    public void validateOnUpdate(Long id, User user) {
        throwExceptionIfModelIsNull(user);
        throwExceptionWhenIdIsNull(user.getId());

        validate(user);
    }

    public void validateAuthorisationToken(Jwt token) {
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Received token is null");
        }
    }
}
