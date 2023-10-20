package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.*;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringIntegrationTest
class ObjectivePersistenceServiceIT {
    Objective createdObjective;
    @Autowired
    private ObjectivePersistenceService objectivePersistenceService;
    @Autowired
    TeamPersistenceService teamPersistenceService;
    @Autowired
    QuarterPersistenceService quarterPersistenceService;

    private static Objective createObjective(Long id) {
        return Objective.Builder.builder().withId(id).withTitle("title")
                .withCreatedBy(User.Builder.builder().withId(1L).build())
                .withTeam(Team.Builder.builder().withId(5L).build())
                .withQuarter(Quarter.Builder.builder().withId(1L).build()).withDescription("This is our description")
                .withState(State.DRAFT).withCreatedOn(LocalDateTime.MAX).withModifiedOn(LocalDateTime.MAX)
                .withModifiedBy(User.Builder.builder().withId(1L).build()).build();
    }

    @AfterEach
    void tearDown() {
        try {
            if (createdObjective != null) {
                objectivePersistenceService.findById(createdObjective.getId());
                objectivePersistenceService.deleteById(createdObjective.getId());
            }
        } catch (ResponseStatusException ex) {
            // created key result already deleted
        } finally {
            createdObjective = null;
        }
    }

    @Test
    void findAllShouldReturnListOfObjectives() {
        List<Objective> objectives = objectivePersistenceService.findAll();

        assertEquals(7, objectives.size());
    }

    @Test
    void findByIdShouldReturnObjectiveProperly() {
        Objective objective = objectivePersistenceService.findById(5L);

        assertEquals(5L, objective.getId());
        assertEquals("Wir wollen das leiseste Team bei Puzzle sein", objective.getTitle());
    }

    @Test
    void findByIdShouldThrowExceptionWhenObjectiveNotFound() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectivePersistenceService.findById(321L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Objective with id 321 not found", exception.getReason());
    }

    @Test
    void findByIdShouldThrowExceptionWhenObjectiveIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectivePersistenceService.findById(null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Missing identifier for Objective", exception.getReason());
    }

    @Test
    void saveObjectiveShouldSaveNewObjective() {
        Objective objective = createObjective(null);

        createdObjective = objectivePersistenceService.save(objective);

        assertNotNull(createdObjective.getId());
        assertEquals(objective.getDescription(), createdObjective.getDescription());
        assertEquals(objective.getDescription(), createdObjective.getDescription());
        assertEquals(objective.getModifiedOn(), createdObjective.getModifiedOn());
    }

    @Test
    void updateObjectiveShouldUpdateObjective() {
        Objective objective = createObjective(null);
        createdObjective = objectivePersistenceService.save(objective);
        createdObjective.setState(State.ONGOING);

        Objective updatedObjective = objectivePersistenceService.save(createdObjective);

        assertEquals(createdObjective.getId(), updatedObjective.getId());
        assertEquals(State.ONGOING, updatedObjective.getState());
    }

    @Test
    void deleteObjectiveShouldThrowExceptionWhenKeyResultNotFound() {
        Objective objective = createObjective(321L);
        createdObjective = objectivePersistenceService.save(objective);
        objectivePersistenceService.deleteById(createdObjective.getId());

        Long objectiveId = createdObjective.getId();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectivePersistenceService.findById(objectiveId));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("Objective with id %d not found", createdObjective.getId()), exception.getReason());
    }

    @Test
    void countByTeamAndQuarterShouldReturnActiveObjectivesOfTeamByQuarter() {
        Integer count = objectivePersistenceService.countByTeamAndQuarter(teamPersistenceService.findById(5L),
                quarterPersistenceService.findById(2L));
        assertEquals(2, count);
    }

    @Test
    void countByTeamAndQuarterShouldThrowErrorIfQuarterDoesNotExist() {
        Team teamId5 = teamPersistenceService.findById(5L);
        Quarter quarterId12 = quarterPersistenceService.findById(12L);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectivePersistenceService.countByTeamAndQuarter(teamId5, quarterId12));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("Quarter with id %d not found", 12), exception.getReason());

        Team teamId500 = teamPersistenceService.findById(500L);
        Quarter quarterId2 = quarterPersistenceService.findById(2L);
        ResponseStatusException exceptionTeam = assertThrows(ResponseStatusException.class,
                () -> objectivePersistenceService.countByTeamAndQuarter(teamId500, quarterId2));
        assertEquals(HttpStatus.NOT_FOUND, exceptionTeam.getStatus());
        assertEquals(String.format("Team with id %d not found", 500), exceptionTeam.getReason());

    }

    @Test
    void countByTeamAndQuarterShouldReturnCountValue() {
        Integer count = objectivePersistenceService.countByTeamAndQuarter(Team.Builder.builder().withId(5L).build(),
                Quarter.Builder.builder().withId(2L).build());

        assertEquals(2, count);
    }
}
