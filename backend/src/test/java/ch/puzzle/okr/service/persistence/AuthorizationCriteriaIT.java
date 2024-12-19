package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.test.TestHelper;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static ch.puzzle.okr.test.TestHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringIntegrationTest
class AuthorizationCriteriaIT {

    @Autowired
    ObjectivePersistenceService objectivePersistenceService;
    @Autowired
    OverviewPersistenceService overviewPersistenceService;

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(TestHelper.SCHEMA_PITC);
    }

    @AfterEach
    void tearDown() {
        TenantContext.setCurrentTenant(null);
    }

    @DisplayName("Should return objective on findObjectiveById() when user is has first level role")
    @Test
    void appendObjectiveShouldReturnObjectiveWhenFirstLevelRole() {
        Long objectiveId = 5L;
        AuthorizationUser authorizationUser = defaultAuthorizationUser();
        Objective objective = objectivePersistenceService.findObjectiveById(objectiveId, authorizationUser, null);

        assertEquals(objectiveId, objective.getId());
    }

    @DisplayName("Should return objective on findObjectiveById() when user is has second level role")
    @Test
    void appendObjectiveShouldReturnObjectiveWhenSecondLevelRole() {
        Long objectiveId = 6L;
        AuthorizationUser authorizationUser = mockAuthorizationUser(defaultUser(null));
        Objective objective = objectivePersistenceService.findObjectiveById(objectiveId, authorizationUser, null);

        assertEquals(objectiveId, objective.getId());
    }

    @DisplayName("Should return objective on findObjectiveById() when user is has member level role")
    @Test
    void appendObjectiveShouldReturnObjectiveWhenMemberRole() {
        Long objectiveId = 6L;
        AuthorizationUser authorizationUser = mockAuthorizationUser(defaultUser(null));
        Objective objective = objectivePersistenceService.findObjectiveById(objectiveId, authorizationUser, null);

        assertEquals(objectiveId, objective.getId());
    }

    @DisplayName("Should return correct overview on getFilteredOverview() when user has first level role and no team ids are supplied")
    @Test
    void appendOverviewShouldReturnObjectiveWhenFirstLevelRoleAndTeamIdsEmpty() {
        Long quarterId = 2L;
        AuthorizationUser authorizationUser = defaultAuthorizationUser();
        List<Overview> overviews = overviewPersistenceService.getFilteredOverview(quarterId, List.of(), "",
                authorizationUser);

        assertEquals(18L, overviews.size());
    }

    @DisplayName("Should return correct overview on getFilteredOverview() when user has second level role")
    @Test
    void appendOverviewShouldReturnObjectiveWhenSecondLevelRole() {
        Long quarterId = 2L;
        AuthorizationUser authorizationUser = mockAuthorizationUser(defaultUser(null));
        List<Overview> overviews = overviewPersistenceService.getFilteredOverview(quarterId, List.of(5L), "",
                authorizationUser);

        assertEquals(6L, overviews.size());
    }

    @DisplayName("Should return correct overview on getFilteredOverview() when user has member level role")
    @Test
    void appendOverviewShouldReturnObjectiveWhenMemberRole() {
        Long quarterId = 2L;
        AuthorizationUser authorizationUser = mockAuthorizationUser(defaultUser(null));
        List<Overview> overviews = overviewPersistenceService.getFilteredOverview(quarterId, List.of(5L), "",
                authorizationUser);

        assertEquals(6L, overviews.size());
    }
}
