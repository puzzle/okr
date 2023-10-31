package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static ch.puzzle.okr.TestHelper.*;
import static ch.puzzle.okr.models.authorization.AuthorizationRole.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringIntegrationTest
class AuthorizationCriteriaIT {
    private final String reason = "not authorized to read objective";
    private final AuthorizationUser authorizationUser = defaultAuthorizationUser();

    @Autowired
    ObjectivePersistenceService objectivePersistenceService;
    @Autowired
    OverviewPersistenceService overviewPersistenceService;

    @Test
    void appendObjective_ShouldReturnObjective_WhenFirstLevelRole() {
        Long objectiveId = 5L;
        AuthorizationUser authorizationUser = defaultAuthorizationUser();
        Objective objective = objectivePersistenceService.findObjectiveById(objectiveId, authorizationUser, reason);

        assertEquals(objectiveId, objective.getId());
    }

    @Test
    void appendObjective_ShouldReturnObjective_WhenSecondLevelRole() {
        Long objectiveId = 6L;
        AuthorizationUser authorizationUser = mockAuthorizationUser(defaultUser(null), List.of(), 5L,
                List.of(READ_ALL_PUBLISHED, READ_TEAMS_DRAFT));
        Objective objective = objectivePersistenceService.findObjectiveById(objectiveId, authorizationUser, reason);

        assertEquals(objectiveId, objective.getId());
    }

    @Test
    void appendObjective_ShouldReturnObjective_WhenMemberRole() {
        Long objectiveId = 6L;
        AuthorizationUser authorizationUser = mockAuthorizationUser(defaultUser(null), List.of(), 5L,
                List.of(READ_ALL_PUBLISHED, READ_TEAM_DRAFT));
        Objective objective = objectivePersistenceService.findObjectiveById(objectiveId, authorizationUser, reason);

        assertEquals(objectiveId, objective.getId());
    }

    @Test
    void appendOverview_ShouldReturnObjective_WhenFirstLevelRoleAndTeamIdsEmpty() {
        Long quarterId = 2L;
        AuthorizationUser authorizationUser = defaultAuthorizationUser();
        List<Overview> overviews = overviewPersistenceService.getFilteredOverview(quarterId, List.of(), "",
                authorizationUser);

        assertEquals(17L, overviews.size());
    }

    @Test
    void appendOverview_ShouldReturnObjective_WhenSecondLevelRole() {
        Long quarterId = 2L;
        AuthorizationUser authorizationUser = mockAuthorizationUser(defaultUser(null), List.of(), 5L,
                List.of(READ_ALL_PUBLISHED, READ_TEAMS_DRAFT));
        List<Overview> overviews = overviewPersistenceService.getFilteredOverview(quarterId, List.of(5L), "",
                authorizationUser);

        assertEquals(6L, overviews.size());
    }

    @Test
    void appendOverview_ShouldReturnObjective_WhenMemberRole() {
        Long quarterId = 2L;
        AuthorizationUser authorizationUser = mockAuthorizationUser(defaultUser(null), List.of(), 5L,
                List.of(READ_ALL_PUBLISHED, READ_TEAM_DRAFT));
        List<Overview> overviews = overviewPersistenceService.getFilteredOverview(quarterId, List.of(5L), "",
                authorizationUser);

        assertEquals(6L, overviews.size());
    }
}
