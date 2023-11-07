package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Organisation;
import ch.puzzle.okr.models.OrganisationState;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.repository.OrganisationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class OrganisationPersistenceService extends PersistenceBase<Organisation, Long, OrganisationRepository> {

    private static final Logger logger = LoggerFactory.getLogger(OrganisationPersistenceService.class);
    private static final String SELECT_ORGANISATION_BY_ID = "SELECT o FROM Organisation o WHERE o.id=:id";
    private final EntityManager entityManager;
    private final AuthorizationCriteria<Organisation> authorizationCriteria;

    protected OrganisationPersistenceService(OrganisationRepository repository, EntityManager entityManager,
            AuthorizationCriteria<Organisation> authorizationCriteria) {
        super(repository);
        this.entityManager = entityManager;
        this.authorizationCriteria = authorizationCriteria;
    }

    @Override
    public String getModelName() {
        return "Organisation";
    }

    public void findOrganisationById(Long organisationId, AuthorizationUser authorizationUser, String reason) {
        findByAnyId(organisationId, authorizationUser, SELECT_ORGANISATION_BY_ID, reason);
    }

    private void findByAnyId(Long id, AuthorizationUser authorizationUser, String queryString, String reason) {
        checkIdNull(id);
        String fullQueryString = queryString + authorizationCriteria.appendObjective(authorizationUser);
        logger.debug("select organisation by id={}: {}", id, fullQueryString);
        TypedQuery<Organisation> typedQuery = entityManager.createQuery(fullQueryString, Organisation.class);
        typedQuery.setParameter("id", id);
        authorizationCriteria.setParameters(typedQuery, authorizationUser);
        try {
            typedQuery.getSingleResult();
        } catch (NoResultException exception) {
            throw new ResponseStatusException(UNAUTHORIZED, reason);
        }
    }

    public Organisation saveIfNotExists(Organisation org) {
        if (!getRepository().existsOrganisationByOrgName(org.getOrgName())) {
            return getRepository().save(org);
        }
        return null;
    }

    public void updateOrganisationStateToInactive(String orgName) {
        Organisation orgInactive = getRepository().findByOrgName(orgName);
        orgInactive.setState(OrganisationState.INACTIVE);
        getRepository().save(orgInactive);
    }
}
