package ch.puzzle.okr.service.business;

import static ch.puzzle.okr.test.TestConstants.TEAM_PUZZLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ch.puzzle.okr.models.alignment.AlignmentSelection;
import ch.puzzle.okr.models.alignment.AlignmentSelectionId;
import ch.puzzle.okr.service.persistence.AlignmentSelectionPersistenceService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AlignmentSelectionBusinessServiceTest {

    @InjectMocks
    AlignmentSelectionBusinessService alignmentSelectionBusinessService;
    @Mock
    AlignmentSelectionPersistenceService alignmentSelectionPersistenceService;

    private static AlignmentSelection createAlignmentSelection() {
        return AlignmentSelection.Builder
                .builder()
                .withAlignmentSelectionId(AlignmentSelectionId.of(9L, 15L))
                .withTeamId(5L)
                .withTeamName(TEAM_PUZZLE)
                .withObjectiveTitle("Objective 9")
                .withQuarterId(2L)
                .withQuarterLabel("GJ 23/24-Q1")
                .withKeyResultTitle("Key Result 15")
                .build();
    }

    @DisplayName("Should get alignment selection by quarter id and all teams except ignored team")
    @Test
    void shouldReturnListOfAlignmentSelectionsUsingGetAlignmentSelectionByQuarterIdAndTeamIdNot() {
        when(alignmentSelectionPersistenceService.getAlignmentSelectionByQuarterIdAndTeamIdNot(2L, 4L))
                .thenReturn(List.of(createAlignmentSelection()));

        List<AlignmentSelection> alignmentSelections = alignmentSelectionBusinessService
                .getAlignmentSelectionByQuarterIdAndTeamIdNot(2L, 4L);

        assertEquals(1, alignmentSelections.size());
        verify(alignmentSelectionPersistenceService, times(1)).getAlignmentSelectionByQuarterIdAndTeamIdNot(2L, 4L);
    }
}
