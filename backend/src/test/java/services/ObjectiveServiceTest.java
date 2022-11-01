package services;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.dto.objectives.GetObjectiveDto;
import ch.puzzle.okr.repository.ObjectiveRepository;
import ch.puzzle.okr.service.ObjectiveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration
@SpringBootConfiguration
class ObjectiveServiceTest {
    @InjectMocks
    private ObjectiveService objectiveService;

    @Mock
    private ObjectiveRepository objectiveRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllObjectivesAsDTOs() {
        Iterable<Objective> objectiveIterable = List.of(new Objective());

        when(this.objectiveRepository.findAll()).thenReturn(objectiveIterable);
        ObjectiveService objectiveServiceSpy = spy(this.objectiveService);

        List<GetObjectiveDto> objectiveDtos = objectiveServiceSpy.getAllObjectives();
        assertEquals(1, objectiveDtos.size());
    }
}
