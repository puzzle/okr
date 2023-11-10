package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.Organisation;
import ch.puzzle.okr.models.authorization.AuthorizationRole;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.OrganisationBusinessService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser();
        if (!authorizationUser.roles().contains(AuthorizationRole.WRITE_ALL)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "not authorized to read organisations");
        }
        return organisationBusinessService.getOrganisations();
    }
}
