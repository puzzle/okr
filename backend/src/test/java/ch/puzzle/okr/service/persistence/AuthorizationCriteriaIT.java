package ch.puzzle.okr.service.persistence;

import java.util.List;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.test.SpringIntegrationTest;
import ch.puzzle.okr.test.TestHelper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Test
    void appendObjectiveShouldReturnObjectiveWhenFirstLevelRole() {
        Long objectiveId = 5L;
        AuthorizationUser authorizationUser = defaultAuthorizationUser();
        Objective objective = objectivePersistenceService.findObjectiveById(objectiveId, authorizationUser, null);

        assertEquals(objectiveId, objective.getId());
    }

    @Test
    void appendObjectiveShouldReturnObjectiveWhenSecondLevelRole() {
        Long objectiveId = 6L;
        AuthorizationUser authorizationUser = mockAuthorizationUser(defaultUser(null));
        Objective objective = objectivePersistenceService.findObjectiveById(objectiveId, authorizationUser, null);

        assertEquals(objectiveId, objective.getId());
    }

    @Test
    void appendObjectiveShouldReturnObjectiveWhenMemberRole() {
        Long objectiveId = 6L;
        AuthorizationUser authorizationUser = mockAuthorizationUser(defaultUser(null));
        Objective objective = objectivePersistenceService.findObjectiveById(objectiveId, authorizationUser, null);

        assertEquals(objectiveId, objective.getId());
    }

    @Test
    void appendOverviewShouldReturnObjectiveWhenFirstLevelRoleAndTeamIdsEmpty() {
        Long quarterId = 2L;
        AuthorizationUser authorizationUser = defaultAuthorizationUser();
        List<Overview> overviews = overviewPersistenceService.getFilteredOverview(quarterId,
                                                                                  List.of(),
                                                                                  "",
                                                                                  authorizationUser);

        assertEquals(18L, overviews.size());
    }

    @Test
    void appendOverviewShouldReturnObjectiveWhenSecondLevelRole() {
        Long quarterId = 2L;
        AuthorizationUser authorizationUser = mockAuthorizationUser(defaultUser(null));
        List<Overview> overviews = overviewPersistenceService.getFilteredOverview(quarterId,
                                                                                  List.of(5L),
                                                                                  "",
                                                                                  authorizationUser);

        assertEquals(6L, overviews.size());
    }

    @Test
    void appendOverviewShouldReturnObjectiveWhenMemberRole() {
        Long quarterId = 2L;
        AuthorizationUser authorizationUser = mockAuthorizationUser(defaultUser(null));
        List<Overview> overviews = overviewPersistenceService.getFilteredOverview(quarterId,
                                                                                  List.of(5L),
                                                                                  "",
                                                                                  authorizationUser);

        assertEquals(6L, overviews.size());
    }
}
