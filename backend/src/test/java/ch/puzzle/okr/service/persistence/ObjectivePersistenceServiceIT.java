package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.TestHelper;
import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

@SpringIntegrationTest
class ObjectivePersistenceServiceIT {
    private static final String REASON = "not authorized to read objective";
    private static final OkrResponseStatusException exception = OkrResponseStatusException.of(REASON);
    private static final String HIGHER_CUSTOMER_HAPPINESS = "Wir wollen die Kundenzufriedenheit steigern";
    private final AuthorizationUser authorizationUser = defaultAuthorizationUser();
    private Objective createdObjective;

    @Autowired
    private ObjectivePersistenceService objectivePersistenceService;
    @Autowired
    private TeamPersistenceService teamPersistenceService;
    @Autowired
    private QuarterPersistenceService quarterPersistenceService;

    private static Objective createObjective(Long id) {
        return createObjective(id, 1);
    }

    private static Objective createObjective(Long id, int version) {
        return Objective.Builder.builder().withId(id).withVersion(version).withTitle("title")
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
    void findObjectiveByIdShouldReturnObjectiveProperly() {
        Objective objective = objectivePersistenceService.findObjectiveById(3L, authorizationUser, exception);

        assertEquals(3L, objective.getId());
        assertEquals(HIGHER_CUSTOMER_HAPPINESS, objective.getTitle());
    }

    @Test
    void findObjectiveByIdShouldThrowExceptionWhenObjectiveNotFound() {
        ResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> objectivePersistenceService.findObjectiveById(321L, authorizationUser,
                        ObjectivePersistenceServiceIT.exception));

        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(REASON, exception.getReason());
    }

    @Test
    void findObjectiveByIdShouldThrowExceptionWhenObjectiveIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> objectivePersistenceService.findObjectiveById(null, authorizationUser,
                        ObjectivePersistenceServiceIT.exception));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Objective")));

        assertEquals(BAD_REQUEST, exception.getStatus());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void findObjectiveByKeyResultIdShouldReturnObjectiveProperly() {
        Objective objective = objectivePersistenceService.findObjectiveByKeyResultId(5L, authorizationUser, exception);

        assertEquals(3L, objective.getId());
        assertEquals(HIGHER_CUSTOMER_HAPPINESS, objective.getTitle());
    }

    @Test
    void findObjectiveByKeyResultIdShouldThrowExceptionWhenObjectiveNotFound() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectivePersistenceService.findObjectiveByKeyResultId(321L, authorizationUser,
                        ObjectivePersistenceServiceIT.exception));

        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(REASON, exception.getReason());
    }

    @Test
    void findObjectiveByKeyResultIdShouldThrowExceptionWhenObjectiveIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> objectivePersistenceService.findObjectiveByKeyResultId(null, authorizationUser,
                        ObjectivePersistenceServiceIT.exception));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Objective")));

        assertEquals(BAD_REQUEST, exception.getStatus());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void findObjectiveByCheckInIdShouldReturnObjectiveProperly() {
        Objective objective = objectivePersistenceService.findObjectiveByCheckInId(7L, authorizationUser, exception);

        assertEquals(3L, objective.getId());
        assertEquals(HIGHER_CUSTOMER_HAPPINESS, objective.getTitle());
    }

    @Test
    void findObjectiveByCheckInIdShouldThrowExceptionWhenObjectiveNotFound() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectivePersistenceService.findObjectiveByCheckInId(321L, authorizationUser,
                        ObjectivePersistenceServiceIT.exception));

        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(REASON, exception.getReason());
    }

    @Test
    void findObjectiveByCheckInIdShouldThrowExceptionWhenObjectiveIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> objectivePersistenceService.findObjectiveByCheckInId(null, authorizationUser,
                        ObjectivePersistenceServiceIT.exception));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Objective")));

        assertEquals(BAD_REQUEST, exception.getStatus());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
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
        createdObjective = objectivePersistenceService.save(createObjective(null));
        Objective updateObjective = createObjective(createdObjective.getId(), createdObjective.getVersion());
        updateObjective.setState(State.ONGOING);

        Objective updatedObjective = objectivePersistenceService.save(updateObjective);

        assertEquals(createdObjective.getId(), updatedObjective.getId());
        assertEquals(State.ONGOING, updatedObjective.getState());
    }

    @Test
    void updateObjectiveShouldThrowExceptionWhenAlreadyUpdated() {
        createdObjective = objectivePersistenceService.save(createObjective(null));
        Objective updateObjective = createObjective(createdObjective.getId(), 0);
        updateObjective.setState(State.ONGOING);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> objectivePersistenceService.save(updateObjective));
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("DATA_HAS_BEEN_UPDATED", List.of("Objective")));

        assertEquals(UNPROCESSABLE_ENTITY, exception.getStatus());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void deleteObjectiveShouldThrowExceptionWhenKeyResultNotFound() {
        Objective objective = createObjective(321L);
        createdObjective = objectivePersistenceService.save(objective);
        objectivePersistenceService.deleteById(createdObjective.getId());

        Long objectiveId = createdObjective.getId();
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> objectivePersistenceService.findById(objectiveId));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_WITH_ID_NOT_FOUND", List.of("Objective", "200")));

        assertEquals(NOT_FOUND, exception.getStatus());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void countByTeamAndQuarterShouldThrowErrorIfQuarterDoesNotExist() {
        Team teamId5 = teamPersistenceService.findById(5L);
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> objectivePersistenceService.countByTeamAndQuarter(teamId5,
                        quarterPersistenceService.findById(12L)));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_WITH_ID_NOT_FOUND", List.of("Quarter", "12")));

        assertEquals(NOT_FOUND, exception.getStatus());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));

        Quarter quarterId2 = quarterPersistenceService.findById(2L);
        OkrResponseStatusException exceptionTeam = assertThrows(OkrResponseStatusException.class,
                () -> objectivePersistenceService.countByTeamAndQuarter(teamPersistenceService.findById(500L),
                        quarterId2));

        List<ErrorDto> expectedErrorsTeam = List.of(new ErrorDto("MODEL_WITH_ID_NOT_FOUND", List.of("Team", "500")));

        assertEquals(NOT_FOUND, exceptionTeam.getStatus());
        assertThat(expectedErrorsTeam).hasSameElementsAs(exceptionTeam.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrorsTeam).contains(exceptionTeam.getReason()));

    }

    @Test
    void countByTeamAndQuarterShouldReturnCountValue() {
        Integer count = objectivePersistenceService.countByTeamAndQuarter(Team.Builder.builder().withId(5L).build(),
                Quarter.Builder.builder().withId(2L).build());

        assertEquals(2, count);
    }
}
