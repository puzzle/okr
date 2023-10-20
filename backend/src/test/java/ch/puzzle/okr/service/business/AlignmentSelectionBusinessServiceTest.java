package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.alignment.AlignmentSelection;
import ch.puzzle.okr.models.alignment.AlignmentSelectionId;
import ch.puzzle.okr.service.persistence.AlignmentSelectionPersistenceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlignmentSelectionBusinessServiceTest {

    @InjectMocks
    AlignmentSelectionBusinessService alignmentSelectionBusinessService;
    @Mock
    AlignmentSelectionPersistenceService alignmentSelectionPersistenceService;

    private static AlignmentSelection createAlignmentSelection() {
        return AlignmentSelection.Builder.builder().withAlignmentSelectionId(AlignmentSelectionId.of(9L, 15L))
                .withTeamId(5L).withTeamName("Puzzle ITC").withObjectiveTitle("Objective 9").withQuarterId(2L)
                .withQuarterLabel("GJ 23/24-Q1").withKeyResultTitle("Key Result 15").build();
    }

    @Test
    void getOverviewByQuarterIdAndTeamIds_ShouldReturnListOfOverviews() {
        when(alignmentSelectionPersistenceService.getAlignmentSelectionByQuarterIdAndTeamIdNot(2L, 4L))
                .thenReturn(List.of(createAlignmentSelection()));

        List<AlignmentSelection> alignmentSelections = alignmentSelectionBusinessService
                .getAlignmentSelectionByQuarterIdAndTeamIdNot(2L, 4L);

        assertEquals(1, alignmentSelections.size());
        verify(alignmentSelectionPersistenceService, times(1)).getAlignmentSelectionByQuarterIdAndTeamIdNot(2L, 4L);
    }
}
