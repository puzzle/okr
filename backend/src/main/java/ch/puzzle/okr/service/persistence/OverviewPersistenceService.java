package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.overview.Overview;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static ch.puzzle.okr.Constants.ARCHIVE_QUARTER_ID;

@Service
public class OverviewPersistenceService {

    private static final Logger logger = LoggerFactory.getLogger(OverviewPersistenceService.class);
    private static final String SELECT_OVERVIEW = "SELECT o FROM Overview o WHERE o.quarterId=:quarterId";
    private static final String SELECT_OVERVIEW_FROM_ARCHIVE = "SELECT o FROM Overview o WHERE o.objectiveArchived=true";

    private final EntityManager entityManager;
    private final AuthorizationCriteria<Overview> authorizationCriteria;

    public OverviewPersistenceService(EntityManager entityManager,
            AuthorizationCriteria<Overview> authorizationCriteria) {
        this.entityManager = entityManager;
        this.authorizationCriteria = authorizationCriteria;
    }

    public List<Overview> getFilteredOverview(Long quarterId, List<Long> teamIds, String objectiveQuery,
            AuthorizationUser authorizationUser) {
        boolean isArchive = Objects.equals(quarterId, ARCHIVE_QUARTER_ID);
        String queryString = createQueryString(teamIds, objectiveQuery, authorizationUser, isArchive);
        return createTypedQuery(quarterId, teamIds, objectiveQuery, authorizationUser, queryString, isArchive);
    }

    private String createQueryString(List<Long> teamIds, String objectiveQuery, AuthorizationUser authorizationUser,
            boolean isArchive) {
        if (isArchive) {
            return SELECT_OVERVIEW_FROM_ARCHIVE
                    + authorizationCriteria.appendOverview(teamIds, objectiveQuery, authorizationUser);
        } else {
            return SELECT_OVERVIEW + authorizationCriteria.appendOverview(teamIds, objectiveQuery, authorizationUser);
        }
    }

    private List<Overview> createTypedQuery(Long quarterId, List<Long> teamIds, String objectiveQuery,
            AuthorizationUser authorizationUser, String queryString, boolean isArchive) {
        TypedQuery<Overview> typedQuery = entityManager.createQuery(queryString, Overview.class);
        if (isArchive) {
            logger.debug("select overview by teamIds={}: {}", teamIds, queryString);
        } else {
            logger.debug("select overview by quarterId={} and teamIds={}: {}", quarterId, teamIds, queryString);
            typedQuery.setParameter("quarterId", quarterId);
        }
        authorizationCriteria.setParameters(typedQuery, teamIds, objectiveQuery, authorizationUser);
        return typedQuery.getResultList();
    }
}
