package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.persistence.TypedQuery;
import java.util.List;

import static ch.puzzle.okr.models.State.*;
import static ch.puzzle.okr.service.authorization.AuthorizationService.*;
import static java.lang.String.format;

@Component
public class AuthorizationCriteria<T> {

    private static final String PARAM_ALL_DRAFT_STATE = "allDraftState";
    private static final String PARAM_FIRST_LEVEL_TEAM_IDS = "firstLevelTeamIds";
    private static final String PARAM_OBJECTIVE_QUERY = "objectiveQuery";
    private static final String PARAM_PUBLISHED_STATES = "publishedStates";
    private static final String PARAM_TEAM_DRAFT_STATE = "teamDraftState";
    private static final String PARAM_TEAMS_DRAFT_STATE = "teamsDraftState";
    private static final String PARAM_TEAM_IDS = "teamIds";
    private static final String PARAM_USER_TEAM_IDS = "userTeamIds";

    public String appendOverview(List<Long> teamIds, String objectiveQuery, AuthorizationUser user) {
        String alias = "o";
        StringBuilder sb = new StringBuilder(256);
        if (shouldAddTeamFilter(teamIds)) {
            sb.append("\n and o.overviewId.teamId in (:" + PARAM_TEAM_IDS + ")");
        }
        if (shouldAddObjectiveFilter(objectiveQuery)) {
            sb.append("\n and lower(coalesce(o.objectiveTitle, '')) like lower(concat('%',:" + PARAM_OBJECTIVE_QUERY
                    + ",'%'))");
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
        if (hasRoleReadAllDraft(user)) {
            sb.append(format(" or %s.%s=:%s", alias, stateColumn, PARAM_ALL_DRAFT_STATE));
        }
        if (hasRoleReadAllPublished(user)) {
            sb.append(format(" or %s.%s IN (:%s)", alias, stateColumn, PARAM_PUBLISHED_STATES));
        }
        if (hasRoleReadTeamDraft(user)) {
            sb.append(format(" or %s.%s=:%s and %s.%s IN (:%s)", alias, stateColumn, PARAM_TEAM_DRAFT_STATE, alias,
                    teamIdColumn, PARAM_USER_TEAM_IDS));
        }
        if (hasRoleReadTeamsDraft(user)) {
            sb.append(format(" or %s.%s=:%s and %s.%s NOT IN (:%s)", alias, stateColumn, PARAM_TEAMS_DRAFT_STATE, alias,
                    teamIdColumn, PARAM_FIRST_LEVEL_TEAM_IDS));
        }
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
        if (hasRoleReadAllDraft(user)) {
            typedQuery.setParameter(PARAM_ALL_DRAFT_STATE, DRAFT);
        }
        if (hasRoleReadAllPublished(user)) {
            typedQuery.setParameter(PARAM_PUBLISHED_STATES, List.of(ONGOING, SUCCESSFUL, NOTSUCCESSFUL));
        }
        if (hasRoleReadTeamDraft(user)) {
            typedQuery.setParameter(PARAM_TEAM_DRAFT_STATE, DRAFT);
            typedQuery.setParameter(PARAM_USER_TEAM_IDS, user.userTeamIds());
        }
        if (hasRoleReadTeamsDraft(user)) {
            typedQuery.setParameter(PARAM_TEAMS_DRAFT_STATE, DRAFT);
            typedQuery.setParameter(PARAM_FIRST_LEVEL_TEAM_IDS, user.firstLevelTeamIds());
        }
    }

    private static boolean shouldAddTeamFilter(List<Long> teamIds) {
        return !CollectionUtils.isEmpty(teamIds);
    }

    private static boolean shouldAddObjectiveFilter(String objectiveQuery) {
        return StringUtils.isNotBlank(objectiveQuery);
    }
}
