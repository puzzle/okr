package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.objectives.GetObjectiveDTO;
import ch.puzzle.okr.models.Objective;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ContextConfiguration
@SpringBootConfiguration
class ObjectiveMapperTest {
    private final ObjectiveMapper objectiveMapper;

    @Autowired
    public ObjectiveMapperTest(ObjectiveMapper objectiveMapper) {
        this.objectiveMapper = objectiveMapper;
    }

    @Test
    void testEntityToGetObjectiveDTO() {
        Objective objective = new Objective();
        GetObjectiveDTO objectiveDto = this.objectiveMapper.entityToGetObjectiveDto(objective);
        assertNull(objectiveDto.getDescription());
    }
}
