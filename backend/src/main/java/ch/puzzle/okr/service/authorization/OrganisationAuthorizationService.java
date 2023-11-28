package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Organisation;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.OrganisationBusinessService;
import org.springframework.stereotype.Service;

import java.util.List;

import static ch.puzzle.okr.Constants.ORGANISATION;
import static ch.puzzle.okr.ErrorKey.NOT_AUTHORIZED_TO_READ;
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
        checkUserAuthorization(OkrResponseStatusException.of(NOT_AUTHORIZED_TO_READ, ORGANISATION));
        return organisationBusinessService.getActiveOrganisations();
    }

    public List<Organisation> getEntitiesByTeam(Long id) {
        checkUserAuthorization(OkrResponseStatusException.of(NOT_AUTHORIZED_TO_READ, ORGANISATION));
        return organisationBusinessService.getOrganisationsByTeam(id);
    }

    public void checkUserAuthorization(OkrResponseStatusException exception) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser();
        if (!hasRoleWriteAll(authorizationUser)) {
            throw exception;
        }
    }
}
