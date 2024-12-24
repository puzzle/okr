package ch.puzzle.okr.mapper;

import static ch.puzzle.okr.test.TestConstants.TEAM_PUZZLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.puzzle.okr.dto.alignment.AlignmentObjectiveDto;
import ch.puzzle.okr.models.alignment.AlignmentSelection;
import ch.puzzle.okr.models.alignment.AlignmentSelectionId;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AlignmentSelectionMapperTest {
    private final AlignmentSelectionMapper alignmentSelectionMapper = new AlignmentSelectionMapper();

    @DisplayName("Should return an empty list when no objectives are found in toDto()")
    @Test
    void shouldReturnEmptyListWhenNoObjectivesAreFoundInToDto() {
        List<AlignmentSelection> alignmentSelections = List.of();
        List<AlignmentObjectiveDto> alignmentObjectiveDtos = alignmentSelectionMapper.toDto(alignmentSelections);

        assertTrue(alignmentObjectiveDtos.isEmpty());
    }

    @DisplayName("Should return one element when a single objective is found in toDto()")
    @Test
    void shouldReturnOneElementWhenSingleObjectiveIsFoundInToDto() {
        List<AlignmentSelection> alignmentSelections = List
                .of(AlignmentSelection.Builder
                        .builder()
                        .withAlignmentSelectionId(AlignmentSelectionId.Builder.builder().withObjectiveId(1L).build())
                        .withTeamId(2L)
                        .withTeamName(TEAM_PUZZLE)
                        .withObjectiveTitle("Objective 1")
                        .build());
        List<AlignmentObjectiveDto> alignmentObjectiveDtos = alignmentSelectionMapper.toDto(alignmentSelections);

        assertEquals(1, alignmentObjectiveDtos.size());
        assertEquals(0, alignmentObjectiveDtos.get(0).keyResults().size());
    }

    @DisplayName("Should return one element when an objective with a key result is found in toDto()")
    @Test
    void shouldReturnOneElementWhenObjectiveWithKeyResultIsFoundInToDto() {
        List<AlignmentSelection> alignmentSelections = List
                .of(AlignmentSelection.Builder
                        .builder()
                        .withAlignmentSelectionId(AlignmentSelectionId.Builder
                                .builder()
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
        assertEquals(1, alignmentObjectiveDtos.get(0).keyResults().size());
    }

    @DisplayName("Should return one element when an objective with multiple key results is found in toDto()")
    @Test
    void shouldReturnOneElementWhenObjectiveWithMultipleKeyResultsIsFoundInToDto() {
        List<AlignmentSelection> alignmentSelections = List
                .of(AlignmentSelection.Builder
                        .builder()
                        .withAlignmentSelectionId(AlignmentSelectionId.Builder
                                .builder()
                                .withObjectiveId(1L)
                                .withKeyResultId(3L)
                                .build())
                        .withTeamId(2L)
                        .withTeamName(TEAM_PUZZLE)
                        .withObjectiveTitle("Objective 1")
                        .withKeyResultTitle("Key Result 3")
                        .build(),
                    AlignmentSelection.Builder
                            .builder()
                            .withAlignmentSelectionId(AlignmentSelectionId.Builder
                                    .builder()
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
        assertEquals(2, alignmentObjectiveDtos.get(0).keyResults().size());
    }

    @DisplayName("Should return multiple objectives with their respective key results in toDto()")
    @Test
    void shouldReturnMultipleObjectivesWithRespectiveKeyResultsInToDto() {
        List<AlignmentSelection> alignmentSelections = List
                .of(AlignmentSelection.Builder
                        .builder()
                        .withAlignmentSelectionId(AlignmentSelectionId.Builder
                                .builder()
                                .withObjectiveId(1L)
                                .withKeyResultId(3L)
                                .build())
                        .withTeamId(2L)
                        .withTeamName(TEAM_PUZZLE)
                        .withObjectiveTitle("Objective 1")
                        .withKeyResultTitle("Key Result 3")
                        .build(),
                    AlignmentSelection.Builder
                            .builder()
                            .withAlignmentSelectionId(AlignmentSelectionId.Builder
                                    .builder()
                                    .withObjectiveId(5L)
                                    .withKeyResultId(6L)
                                    .build())
                            .withTeamId(2L)
                            .withTeamName(TEAM_PUZZLE)
                            .withObjectiveTitle("Objective 5")
                            .withKeyResultTitle("Key Result 6")
                            .build(),
                    AlignmentSelection.Builder
                            .builder()
                            .withAlignmentSelectionId(AlignmentSelectionId.Builder
                                    .builder()
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
        assertEquals(2, alignmentObjectiveDtos.get(0).keyResults().size());
        assertEquals(1, alignmentObjectiveDtos.get(1).keyResults().size());
    }
}
