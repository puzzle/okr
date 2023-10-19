package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;
import java.util.List;

import static ch.puzzle.okr.models.State.*;
import static ch.puzzle.okr.service.authorization.AuthorizationService.*;
import static java.lang.String.format;

@Component
public class AuthorizationCriteria<T> {

    public String appendOverview(AuthorizationUser authorizationUser) {
        return append(authorizationUser, "o", "objectiveState", "overviewId.teamId");
    }

    public String appendObjective(AuthorizationUser authorizationUser) {
        return append(authorizationUser, "o", "state", "team.id");
    }

    private String append(AuthorizationUser user, String alias, String stateColumn, String teamIdColumn) {
        StringBuilder sb = new StringBuilder(256);
        if (hasRoleReadAllDraft(user)) {
            sb.append(format(" or %s.%s=:draftState", alias, stateColumn));
        }
        if (hasRoleReadAllPublished(user)) {
            sb.append(format(" or %s.%s IN (:publishedStates)", alias, stateColumn));
        }
        if (hasRoleReadTeamDraft(user)) {
            sb.append(format(" or %s.%s=:draftState and %s.%s IN (:authorizationTeamId)", alias, stateColumn, alias,
                    teamIdColumn));
        }
        if (hasRoleReadTeamsDraft(user)) {
            sb.append(format(" or %s.%s=:draftState and %s.%s <> :firstLevelTeamId", alias, stateColumn, alias,
                    teamIdColumn));
        }
        if (!sb.isEmpty()) {
            sb.delete(0, 4).insert(0, " and (").append(")");
        }
        return sb.toString();
    }

    public void setParameters(TypedQuery<T> typedQuery, AuthorizationUser user) {
        if (hasRoleReadAllDraft(user)) {
            typedQuery.setParameter("draftState", DRAFT);
        }
        if (hasRoleReadAllPublished(user)) {
            typedQuery.setParameter("publishedStates", List.of(ONGOING, SUCCESSFUL, NOTSUCCESSFUL));
        }
        if (hasRoleReadTeamDraft(user)) {
            typedQuery.setParameter("draftState", DRAFT);
            typedQuery.setParameter("authorizationTeamId", user.teamIds());
        }
        if (hasRoleReadTeamsDraft(user)) {
            typedQuery.setParameter("draftState", DRAFT);
            typedQuery.setParameter("firstLevelTeamId", user.firstLevelTeamId());
        }
    }
}
