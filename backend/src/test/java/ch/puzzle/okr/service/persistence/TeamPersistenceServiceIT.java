package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static ch.puzzle.okr.TestConstants.TEAM_PUZZLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@SpringIntegrationTest
class TeamPersistenceServiceIT {
    Team createdTeam;
    @Autowired
    private TeamPersistenceService teamPersistenceService;

    @AfterEach
    void tearDown() {
        try {
            if (createdTeam != null) {
                teamPersistenceService.findById(createdTeam.getId());
                teamPersistenceService.deleteById(createdTeam.getId());
            }
        } catch (ResponseStatusException ex) {
            // created team already deleted
        } finally {
            createdTeam = null;
        }
    }

    @Test
    void getTeamByIdShouldReturnTeam() throws ResponseStatusException {
        Team team = teamPersistenceService.findById(5L);

        assertEquals(5L, team.getId());
        assertEquals(TEAM_PUZZLE, team.getName());
    }

    @Test
    void getTeamByIdShouldThrowExceptionWhenTeamNotFound() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamPersistenceService.findById(321L));

        assertEquals(NOT_FOUND, exception.getStatus());
        assertEquals("Team with id 321 not found", exception.getReason());
    }

    @Test
    void getTeamByIdShouldThrowExceptionWhenTeamIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamPersistenceService.findById(null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Missing identifier for Team", exception.getReason());
    }

    @Test
    void shouldSaveANewTeam() {
        Team team = Team.Builder.builder().withName("TestTeam").withRoleName("TestRole").build();

        createdTeam = teamPersistenceService.save(team);
        assertNotNull(createdTeam.getId());
        assertEquals("TestTeam", createdTeam.getName());
    }

    @Test
    void shouldUpdateTeamProperly() {
        Team team = Team.Builder.builder().withName("New Team").withRoleName("TestRole").build();
        createdTeam = teamPersistenceService.save(team);
        createdTeam.setName("Updated Team");

        Team returnedTeam = teamPersistenceService.save(createdTeam);

        assertEquals(createdTeam.getId(), returnedTeam.getId());
        assertEquals("Updated Team", returnedTeam.getName());
    }

    @Test
    void shouldDeleteTeam() {
        Team team = Team.Builder.builder().withName("New Team").withRoleName("TestRole").build();
        createdTeam = teamPersistenceService.save(team);
        teamPersistenceService.deleteById(createdTeam.getId());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamPersistenceService.findById(createdTeam.getId()));
        assertEquals(NOT_FOUND, exception.getStatus());
        assertEquals(String.format("Team with id %d not found", createdTeam.getId()), exception.getReason());
    }

    @Test
    void findByRoleName_ShouldReturnTeam_WhenRoleNameFound() {
        Team team = teamPersistenceService.findByRoleName("org_gl");

        assertEquals("Puzzle ITC", team.getName());
        assertEquals("org_gl", team.getRoleName());
    }

    @Test
    void findByRoleName_ShouldThrowException_WhenRoleNameNotFound() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamPersistenceService.findByRoleName("unknown"));

        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals("role name unknown does not match team", exception.getReason());
    }

    @Test
    void findByRoleNames_ShouldReturnSelectedTeamIds() {
        List<Long> teamIds = teamPersistenceService.findByRoleNames(List.of("org_gl", "org_bl", "org_foo"));

        assertThat(List.of(5L, 6L)).hasSameElementsAs(teamIds);
    }
}