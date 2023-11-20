package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.*;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.repository.ObjectiveRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class ObjectivePersistenceService extends PersistenceBase<Objective, Long, ObjectiveRepository> {

    private static final Logger logger = LoggerFactory.getLogger(ObjectivePersistenceService.class);
    private static final String SELECT_OBJECTIVE_BY_ID = "SELECT o FROM Objective o WHERE o.id=:id";
    private static final String SELECT_OBJECTIVE_BY_KEY_RESULT_ID = "SELECT o FROM Objective o LEFT JOIN KeyResult k ON k.objective.id = o.id WHERE k.id=:id";
    private static final String SELECT_OBJECTIVE_BY_CHECK_IN_ID = "SELECT o FROM Objective o LEFT JOIN KeyResult k ON k.objective.id = o.id LEFT JOIN CheckIn c ON c.keyResult.id = k.id WHERE c.id=:id";

    private final EntityManager entityManager;
    private final AuthorizationCriteria<Objective> authorizationCriteria;

    protected ObjectivePersistenceService(ObjectiveRepository repository, EntityManager entityManager,
            AuthorizationCriteria<Objective> authorizationCriteria) {
        super(repository);
        this.entityManager = entityManager;
        this.authorizationCriteria = authorizationCriteria;
    }

    @Override
    public String getModelName() {
        return "Objective";
    }

    public Integer countByTeamAndQuarter(Team team, Quarter quarter) {
        return getRepository().countByTeamAndQuarter(team, quarter);
    }

    public Objective findObjectiveById(Long objectiveId, AuthorizationUser authorizationUser, String reason) {
        return findByAnyId(objectiveId, authorizationUser, SELECT_OBJECTIVE_BY_ID, reason);
    }

    public List<Objective> findObjectiveByTeamId(Long teamId) {
        return getRepository().findObjectivesByTeamId(teamId);
    }

    public Objective findObjectiveByKeyResultId(Long keyResultId, AuthorizationUser authorizationUser, String reason) {
        return findByAnyId(keyResultId, authorizationUser, SELECT_OBJECTIVE_BY_KEY_RESULT_ID, reason);
    }

    public Objective findObjectiveByCheckInId(Long checkInId, AuthorizationUser authorizationUser, String reason) {
        return findByAnyId(checkInId, authorizationUser, SELECT_OBJECTIVE_BY_CHECK_IN_ID, reason);
    }

    private Objective findByAnyId(Long id, AuthorizationUser authorizationUser, String queryString, String reason) {
        checkIdNull(id);
        String fullQueryString = queryString + authorizationCriteria.appendObjective(authorizationUser);
        logger.debug("select objective by id={}: {}", id, fullQueryString);
        TypedQuery<Objective> typedQuery = entityManager.createQuery(fullQueryString, Objective.class);
        typedQuery.setParameter("id", id);
        authorizationCriteria.setParameters(typedQuery, authorizationUser);
        try {
            return typedQuery.getSingleResult();
        } catch (NoResultException exception) {
            throw new OkrResponseStatusException(UNAUTHORIZED, ErrorMsgKey.UNAUTHORIZED,
                    List.of("Objective", id.toString()));
        }
    }
}
