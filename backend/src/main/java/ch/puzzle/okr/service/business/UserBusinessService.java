package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import ch.puzzle.okr.service.validation.UserValidationService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserBusinessService {

    private final UserPersistenceService userPersistenceService;

    private final UserValidationService validationService;

    public UserBusinessService(UserPersistenceService userPersistenceService, UserValidationService validationService) {
        this.userPersistenceService = userPersistenceService;
        this.validationService = validationService;
    }

    public List<User> getAllUsers() {
        return userPersistenceService.findAll();
    }

    public User getOwnerById(Long ownerId) {
        return userPersistenceService.findById(ownerId);
    }

    public User getOrCreateUser(User newUser) {
        validationService.validateOnCreate(newUser);
        return userPersistenceService.getOrCreateUser(newUser);
    }

    public User getUserByAuthorisationToken(Jwt token) {
        validationService.validateAuthorisationToken(token);
        return userPersistenceService.findUserByUsername(token.getClaimAsString("preferred_username"));
    }
}
