package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.TestHelper;
import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static ch.puzzle.okr.TestConstants.TEAM_PUZZLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

@SpringIntegrationTest
class TeamPersistenceServiceIT {

    private static final String NEW_TEAM = "New Team";
    private Team createdTeam;
    @Autowired
    private TeamPersistenceService teamPersistenceService;

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(TestHelper.SCHEMA_PITC);
    }

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
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> teamPersistenceService.findById(321L));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_WITH_ID_NOT_FOUND", List.of("Team", "321")));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void getTeamByIdShouldThrowExceptionWhenTeamIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> teamPersistenceService.findById(null));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Team")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void shouldSaveANewTeam() {
        Team team = Team.Builder.builder().withName("TestTeam").build();

        createdTeam = teamPersistenceService.save(team);
        assertNotNull(createdTeam.getId());
        assertEquals("TestTeam", createdTeam.getName());
    }

    @Test
    void shouldUpdateTeamProperly() {
        Team team = Team.Builder.builder().withName(NEW_TEAM).build();
        createdTeam = teamPersistenceService.save(team);
        createdTeam.setName("Updated Team");

        Team returnedTeam = teamPersistenceService.save(createdTeam);

        assertEquals(createdTeam.getId(), returnedTeam.getId());
        assertEquals("Updated Team", returnedTeam.getName());
    }

    @Test
    void updateTeamShouldThrowExceptionWhenAlreadyUpdated() {
        Team team = Team.Builder.builder().withVersion(1).withName(NEW_TEAM).build();
        createdTeam = teamPersistenceService.save(team);
        Team changedTeam = Team.Builder.builder().withId(createdTeam.getId()).withVersion(0).withName("Changed Team")
                .build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> teamPersistenceService.save(changedTeam));
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("DATA_HAS_BEEN_UPDATED", List.of("Team")));

        assertEquals(UNPROCESSABLE_ENTITY, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void shouldDeleteTeam() {
        Team team = Team.Builder.builder().withName(NEW_TEAM).build();
        createdTeam = teamPersistenceService.save(team);
        teamPersistenceService.deleteById(createdTeam.getId());

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> teamPersistenceService.findById(createdTeam.getId()));

        List<ErrorDto> expectedErrors = List
                .of(ErrorDto.of("MODEL_WITH_ID_NOT_FOUND", List.of("Team", createdTeam.getId())));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void shouldFindTeamsByName() {
        Team team = Team.Builder.builder().withName(NEW_TEAM).build();
        createdTeam = teamPersistenceService.save(team);
        List<Team> teams = teamPersistenceService.findTeamsByName(NEW_TEAM);
        assertThat(teams).contains(createdTeam);
    }
}