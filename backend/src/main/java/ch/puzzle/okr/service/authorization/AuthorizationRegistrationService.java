package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.UserBusinessService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static ch.puzzle.okr.SpringCachingConfig.AUTHORIZATION_USER_CACHE;

@Service
public class AuthorizationRegistrationService {

    private static final String DELIMITER = ",";

    @Value("${okr.user.champion.emails}")
    private String okrChampionEmails;

    private final UserBusinessService userBusinessService;

    public AuthorizationRegistrationService(UserBusinessService userBusinessService) {
        this.userBusinessService = userBusinessService;
    }

    @Cacheable(value = AUTHORIZATION_USER_CACHE, key = "#userFromToken.email")
    public AuthorizationUser updateOrAddAuthorizationUser(User userFromToken) {
        var userFromDB = userBusinessService.getOrCreateUser(userFromToken);
        updateChangeableFields(userFromToken, userFromDB);
        return new AuthorizationUser(userFromDB);
    }

    // firstname and lastname comes from JWT token and could be updated in keycloak.
    // okr champion is set in application properties and should be updated as well
    private void updateChangeableFields(User userFromToken, User userFromDB) {
        userFromDB.setFirstname(userFromToken.getFirstname());
        userFromDB.setLastname(userFromToken.getLastname());
        setOkrChampionFromProperties(userFromDB);
        userBusinessService.saveUser(userFromDB);
    }

    public void setOkrChampionFromProperties(User user) {
        String[] championMails = okrChampionEmails.split(DELIMITER);
        for (var mail : championMails) {
            if (mail.trim().equals(user.getEmail())) {
                user.setOkrChampion(true);
                return;
            }
        }
    }
}
