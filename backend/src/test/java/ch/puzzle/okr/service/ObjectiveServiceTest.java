package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.repository.ObjectiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ObjectiveServiceTest {
    @MockBean
    ObjectiveRepository objectiveRepository = Mockito.mock(ObjectiveRepository.class);

    @InjectMocks
    private ObjectiveService objectiveService;

    Objective objective;
    List<Objective> objectiveList;

    @BeforeEach
    void setUp() {
        this.objective = Objective.Builder.builder()
                .withId(5L)
                .withTitle("Objective 1")
                .build();
        this.objectiveList = List.of(objective, objective, objective);
    }

    @Test
    void shouldGetAllObjectives() {
        Mockito.when(objectiveRepository.findAll()).thenReturn(objectiveList);

        List<Objective> objectives = objectiveService.getAllObjectives();

        assertEquals(3 ,objectives.size());
        assertEquals("Objective 1", objectives.get(0).getTitle());
    }

    @Test
    void shouldReturnEmptyListWhenNoObjective() {
        Mockito.when(objectiveRepository.findAll()).thenReturn(Collections.emptyList());

        List<Objective> objectives = objectiveService.getAllObjectives();

        assertEquals(0 ,objectives.size());
    }
}
