package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.Organisation;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.OrganisationBusinessService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static ch.puzzle.okr.service.authorization.AuthorizationService.hasRoleWriteAll;

@Service
public class OrganisationAuthorizationService {
    private final OrganisationBusinessService organisationBusinessService;
    private final AuthorizationService authorizationService;

    public OrganisationAuthorizationService(OrganisationBusinessService organisationBusinessService,
            AuthorizationService authorizationService) {
        this.organisationBusinessService = organisationBusinessService;
        this.authorizationService = authorizationService;
    }

    public List<Organisation> getEntities() {
        checkUserAuthorization("Not authorized to read organisations");
        return organisationBusinessService.getActiveOrganisations();
    }

    public List<Organisation> getEntitiesByTeam(Long id) {
        checkUserAuthorization("Not authorized to read organisations of team");
        return organisationBusinessService.getOrganisationsByTeam(id);
    }

    public void checkUserAuthorization(String message) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser();
        if (!hasRoleWriteAll(authorizationUser)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, message);
        }
    }
}
