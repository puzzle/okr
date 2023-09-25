package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.alignment.AlignmentObjectiveDto;
import ch.puzzle.okr.models.AlignmentSelection;
import ch.puzzle.okr.models.AlignmentSelectionId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlignmentSelectionMapperTest {
    private final AlignmentSelectionMapper alignmentSelectionMapper = new AlignmentSelectionMapper();

    @Test
    void toDto_ShouldReturnEmptyList_WhenNoObjectiveFound() {
        List<AlignmentSelection> alignmentSelections = List.of();
        List<AlignmentObjectiveDto> alignmentObjectiveDtos = alignmentSelectionMapper.toDto(alignmentSelections);

        assertTrue(alignmentObjectiveDtos.isEmpty());
    }

    @Test
    void toDto_ShouldReturnOneElement_WhenObjectiveFound() {
        List<AlignmentSelection> alignmentSelections = List.of(AlignmentSelection.Builder.builder()
                .withAlignmentSelectionId(AlignmentSelectionId.Builder.builder().withObjectiveId(1L).build())
                .withTeamId(2L).withTeamName("Puzzle ITC").withObjectiveTitle("Objective 1").build());
        List<AlignmentObjectiveDto> alignmentObjectiveDtos = alignmentSelectionMapper.toDto(alignmentSelections);

        assertEquals(1, alignmentObjectiveDtos.size());
        assertEquals(0, alignmentObjectiveDtos.get(0).keyResults().size());
    }

    @Test
    void toDto_ShouldReturnOneElement_WhenObjectiveWithKeyResultFound() {
        List<AlignmentSelection> alignmentSelections = List.of(AlignmentSelection.Builder.builder()
                .withAlignmentSelectionId(
                        AlignmentSelectionId.Builder.builder().withObjectiveId(1L).withKeyResultId(3L).build())
                .withTeamId(2L).withTeamName("Puzzle ITC").withObjectiveTitle("Objective 1")
                .withKeyResultTitle("Key Result 3").build());
        List<AlignmentObjectiveDto> alignmentObjectiveDtos = alignmentSelectionMapper.toDto(alignmentSelections);

        assertEquals(1, alignmentObjectiveDtos.size());
        assertEquals(1, alignmentObjectiveDtos.get(0).keyResults().size());
    }

    @Test
    void toDto_ShouldReturnOneElement_WhenObjectiveWithTwoKeyResultsFound() {
        List<AlignmentSelection> alignmentSelections = List.of(
                AlignmentSelection.Builder.builder()
                        .withAlignmentSelectionId(
                                AlignmentSelectionId.Builder.builder().withObjectiveId(1L).withKeyResultId(3L).build())
                        .withTeamId(2L).withTeamName("Puzzle ITC").withObjectiveTitle("Objective 1")
                        .withKeyResultTitle("Key Result 3").build(),
                AlignmentSelection.Builder.builder()
                        .withAlignmentSelectionId(
                                AlignmentSelectionId.Builder.builder().withObjectiveId(1L).withKeyResultId(5L).build())
                        .withTeamId(2L).withTeamName("Puzzle ITC").withObjectiveTitle("Objective 1")
                        .withKeyResultTitle("Key Result 5").build());
        List<AlignmentObjectiveDto> alignmentObjectiveDtos = alignmentSelectionMapper.toDto(alignmentSelections);

        assertEquals(1, alignmentObjectiveDtos.size());
        assertEquals(2, alignmentObjectiveDtos.get(0).keyResults().size());
    }

    @Test
    void toDto_ShouldReturnOneElement_WhenTwoObjectivesWithKeyResultsFound() {
        List<AlignmentSelection> alignmentSelections = List.of(
                AlignmentSelection.Builder.builder()
                        .withAlignmentSelectionId(
                                AlignmentSelectionId.Builder.builder().withObjectiveId(1L).withKeyResultId(3L).build())
                        .withTeamId(2L).withTeamName("Puzzle ITC").withObjectiveTitle("Objective 1")
                        .withKeyResultTitle("Key Result 3").build(),
                AlignmentSelection.Builder.builder()
                        .withAlignmentSelectionId(
                                AlignmentSelectionId.Builder.builder().withObjectiveId(5L).withKeyResultId(6L).build())
                        .withTeamId(2L).withTeamName("Puzzle ITC").withObjectiveTitle("Objective 5")
                        .withKeyResultTitle("Key Result 6").build(),
                AlignmentSelection.Builder.builder()
                        .withAlignmentSelectionId(
                                AlignmentSelectionId.Builder.builder().withObjectiveId(1L).withKeyResultId(9L).build())
                        .withTeamId(2L).withTeamName("Puzzle ITC").withObjectiveTitle("Objective 1")
                        .withKeyResultTitle("Key Result 9").build());
        List<AlignmentObjectiveDto> alignmentObjectiveDtos = alignmentSelectionMapper.toDto(alignmentSelections);

        assertEquals(2, alignmentObjectiveDtos.size());
        assertEquals(2, alignmentObjectiveDtos.get(0).keyResults().size());
        assertEquals(1, alignmentObjectiveDtos.get(1).keyResults().size());
    }
}
