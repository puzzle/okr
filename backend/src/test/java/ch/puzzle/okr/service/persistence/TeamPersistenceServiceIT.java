package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.repository.TeamRepository;
import ch.puzzle.okr.test.SpringIntegrationTest;
import ch.puzzle.okr.test.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.List;

import static ch.puzzle.okr.Constants.TEAM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringIntegrationTest
class TeamPersistenceServiceIT {

    @MockitoSpyBean
    private TeamRepository teamRepository;

    @Autowired
    @InjectMocks
    private TeamPersistenceService teamPersistenceService;

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(TestHelper.SCHEMA_PITC);
    }

    // uses data from V100_0_0__TestData.sql
    @DisplayName("Should return teams with matching name on findTeamsByName()")
    @Test
    void findTeamsByNameShouldReturnTeamsWithMatchingName() {
        List<Team> teams = teamPersistenceService.findTeamsByName("LoremIpsum");

        assertEquals(1, teams.size());
        assertEquals(6, teams.get(0).getId());
        assertEquals("LoremIpsum", teams.get(0).getName());
    }

    @DisplayName("Should return team on getModelName()")
    @Test
    void getModelNameShouldReturnTeam() {
        assertEquals(TEAM, teamPersistenceService.getModelName());
    }

    @DisplayName("Should soft delete team on deleteTeam() per default")
    @Test
    void deleteTeamShouldSoftDeleteTeam() {
        // given
        long teamId = 100L;
        Team team = TestHelper.defaultTeamWithFiveUsers(teamId);
        teamPersistenceService.save(team);

        teamPersistenceService.deleteById(teamId);
        // then
        assertTrue(teamPersistenceService.findById(teamId).isDeleted());

        Mockito.verify(teamRepository, Mockito.times(1)).markAsDeleted(teamId);
        assertEquals(5, teamPersistenceService.findById(teamId).getUserTeamList().size());
    }
}