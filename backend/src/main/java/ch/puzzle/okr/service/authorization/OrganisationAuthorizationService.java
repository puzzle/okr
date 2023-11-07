package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.Organisation;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.OrganisationBusinessService;
import org.springframework.stereotype.Service;

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
        List<Organisation> organisations = organisationBusinessService.getOrganisations();
        organisations.forEach(organisation -> hasRoleReadById(organisation, authorizationUser));
        return organisations;
    }

    protected void hasRoleReadById(Organisation entity, AuthorizationUser authorizationUser) {
        authorizationService.hasRoleReadByOrganisation(entity.getId(), authorizationUser);
    }

}
