package ch.puzzle.okr.service.persistence;

import static ch.puzzle.okr.models.State.*;
import static ch.puzzle.okr.service.authorization.AuthorizationService.hasRoleWriteAndReadAll;
import static java.lang.String.format;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class AuthorizationCriteria<T> {

    private static final String PARAM_ALL_DRAFT_STATE = "allDraftState";
    private static final String PARAM_OBJECTIVE_QUERY = "objectiveQuery";
    private static final String PARAM_PUBLISHED_STATES = "publishedStates";
    private static final String PARAM_TEAM_DRAFT_STATE = "teamDraftState";
    private static final String PARAM_TEAM_IDS = "teamIds";
    private static final String PARAM_USER_TEAM_IDS = "userTeamIds";

    public String appendOverview(List<Long> teamIds, String objectiveQuery, AuthorizationUser user) {
        String alias = "o";
        StringBuilder sb = new StringBuilder(256);
        if (shouldAddTeamFilter(teamIds)) {
            sb.append("\n and o.overviewId.teamId in (:" + PARAM_TEAM_IDS + ")");
        }
        if (shouldAddObjectiveFilter(objectiveQuery)) {
            sb
                    .append("\n and lower(coalesce(o.objectiveTitle, '')) like lower(concat('%',:"
                            + PARAM_OBJECTIVE_QUERY + ",'%'))");
        }
        String authorizationWhereClause = append(user, alias, "objectiveState", "overviewId.teamId");
        if (!authorizationWhereClause.isEmpty()) {
            sb.append("\n").append(authorizationWhereClause.substring(0, authorizationWhereClause.length() - 1));
            sb.append(format(" or %s.overviewId.objectiveId = -1)", alias));
        }
        return sb.toString();
    }

    public String appendObjective(AuthorizationUser user) {
        return append(user, "o", "state", "team.id");
    }

    private String append(AuthorizationUser user, String alias, String stateColumn, String teamIdColumn) {
        StringBuilder sb = new StringBuilder(256);
        if (hasRoleWriteAndReadAll(user)) {
            sb.append(format(" or %s.%s=:%s", alias, stateColumn, PARAM_ALL_DRAFT_STATE));
        } else {
            // users can read draft state of teams with admin role
            sb
                    .append(format(" or (%s.%s=:%s and %s.%s IN (:%s))",
                                   alias,
                                   stateColumn,
                                   PARAM_TEAM_DRAFT_STATE,
                                   alias,
                                   teamIdColumn,
                                   PARAM_USER_TEAM_IDS));
        }
        // all users can read published state
        sb.append(format(" or %s.%s IN (:%s)", alias, stateColumn, PARAM_PUBLISHED_STATES));
        if (!sb.isEmpty()) {
            sb.delete(0, 4).insert(0, " and (").append(")");
        }
        return sb.toString();
    }

    public void setParameters(TypedQuery<T> typedQuery, List<Long> teamIds, String objectiveQuery,
                              AuthorizationUser user) {
        if (shouldAddTeamFilter(teamIds)) {
            typedQuery.setParameter(PARAM_TEAM_IDS, teamIds);
        }
        if (shouldAddObjectiveFilter(objectiveQuery)) {
            typedQuery.setParameter(PARAM_OBJECTIVE_QUERY, objectiveQuery);
        }
        setParameters(typedQuery, user);
    }

    public void setParameters(TypedQuery<T> typedQuery, AuthorizationUser user) {
        if (hasRoleWriteAndReadAll(user)) {
            typedQuery.setParameter(PARAM_ALL_DRAFT_STATE, DRAFT);
        } else {
            typedQuery.setParameter(PARAM_TEAM_DRAFT_STATE, DRAFT);
            typedQuery.setParameter(PARAM_USER_TEAM_IDS, user.extractTeamIds());
        }
        typedQuery.setParameter(PARAM_PUBLISHED_STATES, List.of(ONGOING, SUCCESSFUL, NOTSUCCESSFUL));
    }

    private static boolean shouldAddTeamFilter(List<Long> teamIds) {
        return !CollectionUtils.isEmpty(teamIds);
    }

    private static boolean shouldAddObjectiveFilter(String objectiveQuery) {
        return StringUtils.isNotBlank(objectiveQuery);
    }
}
