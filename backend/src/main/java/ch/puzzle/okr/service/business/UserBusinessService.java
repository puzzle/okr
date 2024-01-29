package ch.puzzle.okr.service.business;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.service.CacheService;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import ch.puzzle.okr.service.validation.UserValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserBusinessService {

    private final UserPersistenceService userPersistenceService;

    private final UserValidationService validationService;

    private final CacheService cacheService;

    public UserBusinessService(UserPersistenceService userPersistenceService, UserValidationService validationService,
            CacheService cacheService) {
        this.userPersistenceService = userPersistenceService;
        this.validationService = validationService;
        this.cacheService = cacheService;
    }

    public List<User> getAllUsers() {
        return userPersistenceService.findAll();
    }

    public User getUserById(Long ownerId) {
        return userPersistenceService.findById(ownerId);
    }

    public User getOrCreateUser(User user) {
        validationService.validateOnGetOrCreate(user);
        return userPersistenceService.getOrCreateUser(user);
    }

    public User setOkrChampion(User user, boolean okrChampion) {
        if (!okrChampion) {
            checkAtLeastOneOkrChampionExists(user);
        }
        user.setOkrChampion(okrChampion);
        cacheService.emptyAuthorizationUsersCache();
        return userPersistenceService.save(user);
    }

    // checks if at least one okr champion remains after removing given one
    private void checkAtLeastOneOkrChampionExists(User user) {
        var champions = userPersistenceService.findAllOkrChampions();
        champions.stream().filter(c -> c.isOkrChampion() && !Objects.equals(c.getId(), user.getId())).findAny()
                .orElseThrow(() -> new OkrResponseStatusException(HttpStatus.BAD_REQUEST,
                        ErrorKey.TRIED_TO_REMOVE_LAST_OKR_CHAMPION));
    }
}
