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
        return append(authorizationUser, "o", "objectiveState", "overviewId.teamId", true);
    }

    public String appendObjective(AuthorizationUser authorizationUser) {
        return append(authorizationUser, "o", "state", "team.id", false);
    }

    private String append(AuthorizationUser user, String alias, String stateColumn, String teamIdColumn,
            boolean skipObjective) {
        StringBuilder sb = new StringBuilder(256);
        if (hasRoleReadAllDraft(user)) {
            sb.append(format(" or %s.%s=:allDraftState", alias, stateColumn));
        }
        if (hasRoleReadAllPublished(user)) {
            sb.append(format(" or %s.%s IN (:publishedStates)", alias, stateColumn));
        }
        if (hasRoleReadTeamDraft(user)) {
            sb.append(format(" or %s.%s=:teamDraftState and %s.%s IN (:userTeamIds)", alias, stateColumn, alias,
                    teamIdColumn));
        }
        if (hasRoleReadTeamsDraft(user)) {
            sb.append(format(" or %s.%s=:teamsDraftState and %s.%s NOT IN (:firstLevelTeamIds)", alias, stateColumn,
                    alias, teamIdColumn));
        }
        if (skipObjective) {
            sb.append(format(" or %s.overviewId.objectiveId = -1", alias));
        }
        if (!sb.isEmpty()) {
            sb.delete(0, 4).insert(0, " and (").append(")");
        }
        return sb.toString();
    }

    public void setParameters(TypedQuery<T> typedQuery, AuthorizationUser user) {
        if (hasRoleReadAllDraft(user)) {
            typedQuery.setParameter("allDraftState", DRAFT);
        }
        if (hasRoleReadAllPublished(user)) {
            typedQuery.setParameter("publishedStates", List.of(ONGOING, SUCCESSFUL, NOTSUCCESSFUL));
        }
        if (hasRoleReadTeamDraft(user)) {
            typedQuery.setParameter("teamDraftState", DRAFT);
            typedQuery.setParameter("userTeamIds", user.userTeamIds());
        }
        if (hasRoleReadTeamsDraft(user)) {
            typedQuery.setParameter("teamsDraftState", DRAFT);
            typedQuery.setParameter("firstLevelTeamIds", user.firstLevelTeamIds());
        }
    }
}
