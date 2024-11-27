package ch.puzzle.okr.mapper;

import java.util.List;

import ch.puzzle.okr.dto.alignment.AlignmentObjectiveDto;
import ch.puzzle.okr.models.alignment.AlignmentSelection;
import ch.puzzle.okr.models.alignment.AlignmentSelectionId;

import org.junit.jupiter.api.Test;

import static ch.puzzle.okr.test.TestConstants.TEAM_PUZZLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlignmentSelectionMapperTest {
    private final AlignmentSelectionMapper alignmentSelectionMapper = new AlignmentSelectionMapper();

    @Test
    void toDtoShouldReturnEmptyListWhenNoObjectiveFound() {
        List<AlignmentSelection> alignmentSelections = List.of();
        List<AlignmentObjectiveDto> alignmentObjectiveDtos = alignmentSelectionMapper.toDto(alignmentSelections);

        assertTrue(alignmentObjectiveDtos.isEmpty());
    }

    @Test
    void toDtoShouldReturnOneElementWhenObjectiveFound() {
        List<AlignmentSelection> alignmentSelections = List.of(AlignmentSelection.Builder.builder()
                                                                                         .withAlignmentSelectionId(AlignmentSelectionId.Builder.builder()
                                                                                                                                               .withObjectiveId(1L)
                                                                                                                                               .build())
                                                                                         .withTeamId(2L)
                                                                                         .withTeamName(TEAM_PUZZLE)
                                                                                         .withObjectiveTitle("Objective 1")
                                                                                         .build());
        List<AlignmentObjectiveDto> alignmentObjectiveDtos = alignmentSelectionMapper.toDto(alignmentSelections);

        assertEquals(1, alignmentObjectiveDtos.size());
        assertEquals(0,
                     alignmentObjectiveDtos.get(0)
                                           .keyResults()
                                           .size());
    }

    @Test
    void toDtoShouldReturnOneElementWhenObjectiveWithKeyResultFound() {
        List<AlignmentSelection> alignmentSelections = List.of(AlignmentSelection.Builder.builder()
                                                                                         .withAlignmentSelectionId(AlignmentSelectionId.Builder.builder()
                                                                                                                                               .withObjectiveId(1L)
                                                                                                                                               .withKeyResultId(3L)
                                                                                                                                               .build())
                                                                                         .withTeamId(2L)
                                                                                         .withTeamName(TEAM_PUZZLE)
                                                                                         .withObjectiveTitle("Objective 1")
                                                                                         .withKeyResultTitle("Key Result 3")
                                                                                         .build());
        List<AlignmentObjectiveDto> alignmentObjectiveDtos = alignmentSelectionMapper.toDto(alignmentSelections);

        assertEquals(1, alignmentObjectiveDtos.size());
        assertEquals(1,
                     alignmentObjectiveDtos.get(0)
                                           .keyResults()
                                           .size());
    }

    @Test
    void toDtoShouldReturnOneElementWhenObjectiveWithTwoKeyResultsFound() {
        List<AlignmentSelection> alignmentSelections = List.of(AlignmentSelection.Builder.builder()
                                                                                         .withAlignmentSelectionId(AlignmentSelectionId.Builder.builder()
                                                                                                                                               .withObjectiveId(1L)
                                                                                                                                               .withKeyResultId(3L)
                                                                                                                                               .build())
                                                                                         .withTeamId(2L)
                                                                                         .withTeamName(TEAM_PUZZLE)
                                                                                         .withObjectiveTitle("Objective 1")
                                                                                         .withKeyResultTitle("Key Result 3")
                                                                                         .build(),
                                                               AlignmentSelection.Builder.builder()
                                                                                         .withAlignmentSelectionId(AlignmentSelectionId.Builder.builder()
                                                                                                                                               .withObjectiveId(1L)
                                                                                                                                               .withKeyResultId(5L)
                                                                                                                                               .build())
                                                                                         .withTeamId(2L)
                                                                                         .withTeamName(TEAM_PUZZLE)
                                                                                         .withObjectiveTitle("Objective 1")
                                                                                         .withKeyResultTitle("Key Result 5")
                                                                                         .build());
        List<AlignmentObjectiveDto> alignmentObjectiveDtos = alignmentSelectionMapper.toDto(alignmentSelections);

        assertEquals(1, alignmentObjectiveDtos.size());
        assertEquals(2,
                     alignmentObjectiveDtos.get(0)
                                           .keyResults()
                                           .size());
    }

    @Test
    void toDtoShouldReturnOneElementWhenTwoObjectivesWithKeyResultsFound() {
        List<AlignmentSelection> alignmentSelections = List.of(AlignmentSelection.Builder.builder()
                                                                                         .withAlignmentSelectionId(AlignmentSelectionId.Builder.builder()
                                                                                                                                               .withObjectiveId(1L)
                                                                                                                                               .withKeyResultId(3L)
                                                                                                                                               .build())
                                                                                         .withTeamId(2L)
                                                                                         .withTeamName(TEAM_PUZZLE)
                                                                                         .withObjectiveTitle("Objective 1")
                                                                                         .withKeyResultTitle("Key Result 3")
                                                                                         .build(),
                                                               AlignmentSelection.Builder.builder()
                                                                                         .withAlignmentSelectionId(AlignmentSelectionId.Builder.builder()
                                                                                                                                               .withObjectiveId(5L)
                                                                                                                                               .withKeyResultId(6L)
                                                                                                                                               .build())
                                                                                         .withTeamId(2L)
                                                                                         .withTeamName(TEAM_PUZZLE)
                                                                                         .withObjectiveTitle("Objective 5")
                                                                                         .withKeyResultTitle("Key Result 6")
                                                                                         .build(),
                                                               AlignmentSelection.Builder.builder()
                                                                                         .withAlignmentSelectionId(AlignmentSelectionId.Builder.builder()
                                                                                                                                               .withObjectiveId(1L)
                                                                                                                                               .withKeyResultId(9L)
                                                                                                                                               .build())
                                                                                         .withTeamId(2L)
                                                                                         .withTeamName(TEAM_PUZZLE)
                                                                                         .withObjectiveTitle("Objective 1")
                                                                                         .withKeyResultTitle("Key Result 9")
                                                                                         .build());
        List<AlignmentObjectiveDto> alignmentObjectiveDtos = alignmentSelectionMapper.toDto(alignmentSelections);

        assertEquals(2, alignmentObjectiveDtos.size());
        assertEquals(2,
                     alignmentObjectiveDtos.get(0)
                                           .keyResults()
                                           .size());
        assertEquals(1,
                     alignmentObjectiveDtos.get(1)
                                           .keyResults()
                                           .size());
    }
}
