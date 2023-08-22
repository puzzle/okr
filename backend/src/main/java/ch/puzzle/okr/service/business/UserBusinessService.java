package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.service.ValidationService;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserBusinessService {

    private final UserPersistenceService userPersistenceService;

    private final ValidationService validationService;

    public UserBusinessService(UserPersistenceService userPersistenceService, ValidationService validationService) {
        this.userPersistenceService = userPersistenceService;
        this.validationService = validationService;
    }

    public List<User> getAllUsers() {
        return userPersistenceService.getAllUsers();
    }

    public User getOwnerById(Long ownerId) {
        return userPersistenceService.getOwnerById(ownerId);
    }

    @Cacheable(value = "users", key = "#newUser.username")
    public User getOrCreateUser(User newUser) {
        validationService.validateOnSave(newUser);
        return userPersistenceService.getOrCreateUser(newUser);
    }
}
