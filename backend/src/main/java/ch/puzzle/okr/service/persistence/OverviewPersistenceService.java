package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.overview.Overview;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
public class OverviewPersistenceService {

    private static final Logger logger = LoggerFactory.getLogger(OverviewPersistenceService.class);
    private static final String SELECT_OVERVIEW = """
            SELECT o FROM Overview o
            WHERE o.quarterId=:quarterId
              and o.overviewId.teamId in (:teamIds)
              and (lower(coalesce(o.objectiveTitle, '')) like lower(concat('%',:objectiveQuery,'%'))
                   or o.overviewId.objectiveId = -1)
            """;

    private final EntityManager entityManager;
    private final AuthorizationCriteria<Overview> authorizationCriteria;

    public OverviewPersistenceService(EntityManager entityManager,
            AuthorizationCriteria<Overview> authorizationCriteria) {
        this.entityManager = entityManager;
        this.authorizationCriteria = authorizationCriteria;
    }

    public List<Overview> getFilteredOverview(Long quarterId, List<Long> teamIds, String objectiveQuery,
            AuthorizationUser authorizationUser) {
        String queryString = SELECT_OVERVIEW + authorizationCriteria.appendOverview(authorizationUser);
        logger.debug("select overview by quarterId={} and teamIds={}: {}", quarterId, teamIds, queryString);
        TypedQuery<Overview> typedQuery = entityManager.createQuery(queryString, Overview.class);
        typedQuery.setParameter("quarterId", quarterId);
        typedQuery.setParameter("teamIds", teamIds);
        typedQuery.setParameter("objectiveQuery", objectiveQuery);
        authorizationCriteria.setParameters(typedQuery, authorizationUser);
        return typedQuery.getResultList();
    }
}
