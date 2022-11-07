package ch.puzzle.okr.service;

import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.repository.ObjectiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ObjectiveServiceTest {
    @MockBean
    ObjectiveRepository objectiveRepository = Mockito.mock(ObjectiveRepository.class);
    @MockBean
    KeyResultRepository keyResultRepository = Mockito.mock(KeyResultRepository.class);

    @InjectMocks
    private ObjectiveService objectiveService;

    Objective objective;
    KeyResult keyResult;
    List<Objective> objectiveList;
    List<KeyResult> keyResults;

    @BeforeEach
    void setUp() {
        this.objective = Objective.Builder.builder()
                .withId(5L)
                .withTitle("Objective 1")
                .build();
        this.objectiveList = List.of(objective, objective, objective);
        this.keyResult = KeyResult.Builder.builder()
                .withId(5L)
                .withTitle("Keyresult 1")
                .build();
        this.keyResults = List.of(keyResult, keyResult, keyResult);
    }

    @Test
    void shouldGetAllObjectives() {
        when(objectiveRepository.findAll()).thenReturn(objectiveList);

        List<Objective> objectives = objectiveService.getAllObjectives();

        assertEquals(3 ,objectives.size());
        assertEquals("Objective 1", objectives.get(0).getTitle());
    }

    @Test
    void shouldReturnEmptyListWhenNoObjective() {
        when(objectiveRepository.findAll()).thenReturn(Collections.emptyList());

        List<Objective> objectives = objectiveService.getAllObjectives();

        assertEquals(0 ,objectives.size());
    }

    @Test
    void shouldGetAllKeyresultsByObjective() {
        when(objectiveRepository.findById(1L)).thenReturn(Optional.of(objective));
        when(keyResultRepository.findByObjective(any())).thenReturn(keyResults);

        List<KeyResult> keyResultList = objectiveService.getAllKeyResultsByObjective(1);

        assertEquals(3 ,keyResultList.size());
        assertEquals("Keyresult 1", keyResultList.get(0).getTitle());
    }

    @Test
    void shouldReturnEmptyListWhenNoKeyResultInObjective() {
        when(objectiveRepository.findById(1L)).thenReturn(Optional.of(objective));
        when(keyResultRepository.findByObjective(any())).thenReturn(Collections.emptyList());

        List<KeyResult> keyResultList = objectiveService.getAllKeyResultsByObjective(1);

        assertEquals(0, keyResultList.size());
    }

    @Test
    void shouldThrowExceptionWhenObjectiveDoesntExist() {
        assertThrows(ResponseStatusException.class, () ->
                objectiveService.getAllKeyResultsByObjective(1));
    }

    @Test
    void getOneObjective() {
        Mockito.when(objectiveRepository.findById(5L)).thenReturn(Optional.ofNullable(this.objective));
        Objective realObjective = objectiveService.getObjective(5L);

        assertEquals("Objective 1" , realObjective.getTitle());
    }

    @Test
    void shouldNotFindTheObjective() {
        Mockito.when(objectiveRepository.findById(6L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectiveService.getObjective(6L));
        assertEquals(404, exception.getRawStatusCode());
        assertEquals("Objective with id 6 not found", exception.getReason());
    }


}
