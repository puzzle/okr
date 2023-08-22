package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static ch.puzzle.okr.Constants.TEAM_PUZZLE;
import static org.junit.jupiter.api.Assertions.*;

@SpringIntegrationTest
class TeamPersistenceServiceIT {
    Team createdTeam;
    @Autowired
    private TeamPersistenceService teamPersistenceService;

    @AfterEach
    void tearDown() {
        try {
            if (createdTeam != null) {
                teamPersistenceService.getTeamById(createdTeam.getId());
                teamPersistenceService.deleteTeamById(createdTeam.getId());
            }
        } catch (ResponseStatusException ex) {
            // created team already deleted
        } finally {
            createdTeam = null;
        }
    }

    @Test
    void getTeamById_ShouldReturnTeam() throws ResponseStatusException {
        Team team = teamPersistenceService.getTeamById(5L);

        assertEquals(5L, team.getId());
        assertEquals(TEAM_PUZZLE, team.getName());
    }

    @Test
    void getTeamById_ShouldThrowExceptionWhenTeamNotFound() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamPersistenceService.getTeamById(321L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Team with id 321 not found", exception.getReason());
    }

    @Test
    void getTeamById_ShouldThrowExceptionWhenTeamIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamPersistenceService.getTeamById(null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Missing attribute team id", exception.getReason());
    }

    @Test
    void getTeamByName_ShouldReturnTeam() {
        Optional<Team> team = teamPersistenceService.getTeamByName(TEAM_PUZZLE);

        assertTrue(team.isPresent());
        assertEquals(5L, team.get().getId());
        assertEquals(TEAM_PUZZLE, team.get().getName());
    }

    @Test
    void getTeamByName_ShouldReturnOptionalEmptyWhenTeamNameNotFound() {
        assertEquals(Optional.empty(), teamPersistenceService.getTeamByName("unknown"));
    }

    @Test
    void getTeamsByExcludedName_ShouldReturnTeams() {
        List<Team> teams = teamPersistenceService.getTeamsByExcludedName(TEAM_PUZZLE);

        assertEquals(3, teams.size());
    }

    @Test
    void getTeamsByIdsAndExcludedName_ShouldReturnTeams() {
        List<Team> teams = teamPersistenceService.getTeamsByIdsAndExcludedName(List.of(4L, 8L, 5L), TEAM_PUZZLE);

        assertEquals(2, teams.size());
    }

    @Test
    void shouldSaveANewTeam() {
        Team team = Team.Builder.builder().withName("TestTeam").build();

        createdTeam = teamPersistenceService.saveTeam(team);
        assertNotNull(createdTeam.getId());
        assertEquals("TestTeam", createdTeam.getName());
    }

    @Test
    void shouldUpdateTeamProperly() {
        Team team = Team.Builder.builder().withName("New Team").build();
        createdTeam = teamPersistenceService.saveTeam(team);
        createdTeam.setName("Updated Team");

        Team returnedTeam = teamPersistenceService.updateTeam(createdTeam.getId(), createdTeam);

        assertEquals(createdTeam.getId(), returnedTeam.getId());
        assertEquals("Updated Team", returnedTeam.getName());
    }

    @Test
    void shouldDeleteTeam() {
        Team team = Team.Builder.builder().withName("New Team").build();
        createdTeam = teamPersistenceService.saveTeam(team);
        teamPersistenceService.deleteTeamById(createdTeam.getId());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamPersistenceService.getTeamById(createdTeam.getId()));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("Team with id %d not found", createdTeam.getId()), exception.getReason());
    }
}