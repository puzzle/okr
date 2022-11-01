package ch.puzzle.okr.mapper;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.dto.objectives.GetObjectiveDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ContextConfiguration
@SpringBootConfiguration
class ObjectiveMapperTest {
    private ObjectiveMapper objectiveMapper;

    @BeforeEach
    void setUp() {
        this.objectiveMapper = new ObjectiveMapper();
    }

    @Test
    void testEntityToGetObjectiveDTO() {
        Objective objective = new Objective();
        GetObjectiveDTO objectiveDto = this.objectiveMapper.entityToGetObjectiveDto(objective);
        assertNull(objectiveDto.getDescription());
    }
}
