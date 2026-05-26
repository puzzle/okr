package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.overview.Overview;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OverviewPersistenceService {
    private final QuarterPersistenceService quarterPersistenceService;

    private static final Logger logger = LoggerFactory.getLogger(OverviewPersistenceService.class);
    private static final String SELECT_OVERVIEW =
            "SELECT o FROM Overview o " +
                    "WHERE o.quarterId=:quarterId " +
                    "AND (o.teamMarkedAsArchivedAt IS NULL OR o.teamMarkedAsArchivedAt >= :quarterStartDate)";

    private final EntityManager entityManager;
    private final AuthorizationCriteria<Overview> authorizationCriteria;

    public OverviewPersistenceService(EntityManager entityManager,
                                      AuthorizationCriteria<Overview> authorizationCriteria, QuarterPersistenceService quarterPersistenceService) {
        this.entityManager = entityManager;
        this.authorizationCriteria = authorizationCriteria;
        this.quarterPersistenceService = quarterPersistenceService;
    }

    public List<Overview> getFilteredOverview(Long quarterId, List<Long> teamIds, String objectiveQuery,
                                              AuthorizationUser authorizationUser) {
        LocalDate quarterStartDate = quarterPersistenceService.findById(quarterId).getStartDate();

        String queryString = SELECT_OVERVIEW
                + authorizationCriteria.appendOverview(teamIds, objectiveQuery, authorizationUser);

        logger.debug("select overview by quarterId={} and teamIds={}: {}", quarterId, teamIds, queryString);
        TypedQuery<Overview> typedQuery = entityManager.createQuery(queryString, Overview.class);

        typedQuery.setParameter("quarterId", quarterId);
        typedQuery.setParameter("quarterStartDate", quarterStartDate);

        authorizationCriteria.setParameters(typedQuery, teamIds, objectiveQuery, authorizationUser);
        return typedQuery.getResultList();
    }
}
