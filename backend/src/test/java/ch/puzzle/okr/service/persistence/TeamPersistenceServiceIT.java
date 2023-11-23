package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Stream;

import static ch.puzzle.okr.TestConstants.TEAM_PUZZLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@SpringIntegrationTest
class TeamPersistenceServiceIT {

    private static final Logger logger = LoggerFactory.getLogger(TeamPersistenceServiceIT.class);

    private Team createdTeam;
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
        Team team = Team.Builder.builder().withName("TestTeam").build();

        createdTeam = teamPersistenceService.save(team);
        assertNotNull(createdTeam.getId());
        assertEquals("TestTeam", createdTeam.getName());
    }

    @Test
    void shouldUpdateTeamProperly() {
        Team team = Team.Builder.builder().withName("New Team").build();
        createdTeam = teamPersistenceService.save(team);
        createdTeam.setName("Updated Team");

        Team returnedTeam = teamPersistenceService.save(createdTeam);

        assertEquals(createdTeam.getId(), returnedTeam.getId());
        assertEquals("Updated Team", returnedTeam.getName());
    }

    @Test
    void updateTeamShouldThrowExceptionWhenAlreadyUpdated() {
        Team team = Team.Builder.builder().withVersion(1).withName("New Team").build();
        createdTeam = teamPersistenceService.save(team);
        Team changedTeam = Team.Builder.builder().withId(createdTeam.getId()).withVersion(0).withName("Changed Team")
                .build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamPersistenceService.save(changedTeam));
        assertEquals(UNPROCESSABLE_ENTITY, exception.getStatus());
        assertTrue(exception.getReason().contains("updated or deleted by another user"));
    }

    @Test
    void shouldDeleteTeam() {
        Team team = Team.Builder.builder().withName("New Team").build();
        createdTeam = teamPersistenceService.save(team);
        teamPersistenceService.deleteById(createdTeam.getId());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> teamPersistenceService.findById(createdTeam.getId()));
        assertEquals(NOT_FOUND, exception.getStatus());
        assertEquals(String.format("Team with id %d not found", createdTeam.getId()), exception.getReason());
    }

    @ParameterizedTest
    @MethodSource("findTeamIdsByOrganisationNamesArguments")
    void findTeamIdsByOrganisationNamesShouldReturnFirstLevelTeams(List<String> organisationNames,
            List<Long> expectedIds, String useCase) {
        logger.info(useCase);
        List<Long> teamIds = teamPersistenceService.findTeamIdsByOrganisationNames(organisationNames);

        assertThat(expectedIds).hasSameElementsAs(teamIds);
    }

    @Test
    void shouldFindTeamsByName() {
        Team team = Team.Builder.builder().withName("New Team").build();
        createdTeam = teamPersistenceService.save(team);
        List<Team> teams = teamPersistenceService.findTeamsByName("New Team");
        assertThat(teams).contains(createdTeam);
    }

    private static Stream<Arguments> findTeamIdsByOrganisationNamesArguments() {
        return Stream.of(arguments(List.of("org_gl"), List.of(5L), "get 1st level team"), //
                arguments(List.of("org_bl"), List.of(6L, 8L), "get 2nd level teams"), //
                arguments(List.of("org_mobility"), List.of(6L), "get teams belong to organisation mobility"), //
                arguments(List.of("org_inactive"), List.of(4L, 8L), "get teams belong to inactive organisation name"), //
                arguments(List.of("org_azubi"), List.of(), "get empty team list for organisation azubi"), //
                arguments(List.of("org_unknown"), List.of(), "get teams belong to unknown organisation name") //
        );
    }
}