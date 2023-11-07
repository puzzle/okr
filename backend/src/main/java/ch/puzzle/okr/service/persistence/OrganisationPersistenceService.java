package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Organisation;
import ch.puzzle.okr.models.OrganisationState;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.repository.OrganisationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class OrganisationPersistenceService extends PersistenceBase<Organisation, Long, OrganisationRepository> {
    private final EntityManager entityManager;
    private final AuthorizationCriteria<Organisation> authorizationCriteria;
    private static final String SELECT_ORGANISATION_BY_ID = "SELECT o FROM Organisation o WHERE o.id=:id";

    protected OrganisationPersistenceService(OrganisationRepository repository, EntityManager entityManager,
            AuthorizationCriteria<Organisation> authorizationCriteria) {
        super(repository);
        this.entityManager = entityManager;
        this.authorizationCriteria = authorizationCriteria;
    }

    public Organisation findOrganisationById(Long organisationId, AuthorizationUser authorizationUser, String reason) {
        return findByAnyId(organisationId, authorizationUser, SELECT_ORGANISATION_BY_ID, reason);
    }

    @Override
    public String getModelName() {
        return "Organisation";
    }

    private Organisation findByAnyId(Long id, AuthorizationUser authorizationUser, String queryString, String reason) {
        checkIdNull(id);
        String fullQueryString = queryString + authorizationCriteria.appendObjective(authorizationUser);
        logger.debug("select objective by id={}: {}", id, fullQueryString);
        TypedQuery<Objective> typedQuery = entityManager.createQuery(fullQueryString, Objective.class);
        typedQuery.setParameter("id", id);
        authorizationCriteria.setParameters(typedQuery, authorizationUser);
        try {
            return typedQuery.getSingleResult();
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
