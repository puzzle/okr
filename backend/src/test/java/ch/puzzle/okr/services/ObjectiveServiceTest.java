package ch.puzzle.okr.services;

import ch.puzzle.okr.mapper.ObjectiveMapper;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.dto.objectives.GetObjectiveDTO;
import ch.puzzle.okr.repository.ObjectiveRepository;
import ch.puzzle.okr.service.ObjectiveService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration
@SpringBootConfiguration
class ObjectiveServiceTest {
    @InjectMocks
    private ObjectiveService objectiveService;

    @Mock
    private ObjectiveRepository objectiveRepository;

    @Mock
    private ObjectiveMapper objectiveMapper;

    @Test
    void getAllObjectivesAsDTOs() {
        Iterable<Objective> objectiveIterable = List.of(new Objective());

        when(this.objectiveRepository.findAll()).thenReturn(objectiveIterable);
        when(this.objectiveMapper.entityToGetObjectiveDto(any())).thenReturn(new GetObjectiveDTO());

        List<GetObjectiveDTO> objectiveDtos = this.objectiveService.getAllObjectives();
        assertEquals(1, objectiveDtos.size());
    }
}
