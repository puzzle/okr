package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.alignment.AlignmentSelection;
import ch.puzzle.okr.models.alignment.AlignmentSelectionId;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringIntegrationTest
class AlignmentSelectionPersistenceServiceIT {
    @Autowired
    private AlignmentSelectionPersistenceService alignmentSelectionPersistenceService;

    @Test
    void getAlignmentSelectionByQuarterIdAndTeamIdNotShouldReturnAlignmentSelections() {
        List<AlignmentSelection> alignmentSelections = alignmentSelectionPersistenceService
                .getAlignmentSelectionByQuarterIdAndTeamIdNot(2L, 4L);

        assertEquals(12, alignmentSelections.size());
        alignmentSelections.forEach(alignmentSelection -> assertTrue(
                matchAlignmentSelectionId(alignmentSelection.getAlignmentSelectionId())));
    }

    private boolean matchAlignmentSelectionId(AlignmentSelectionId alignmentSelectionId) {
        return getExpectedAlignmentSelectionIds().anyMatch(id -> id.equals(alignmentSelectionId));
    }

    private static Stream<AlignmentSelectionId> getExpectedAlignmentSelectionIds() {
        return Stream.of(AlignmentSelectionId.of(9L, 15L), //
                AlignmentSelectionId.of(9L, 16L), //
                AlignmentSelectionId.of(9L, 17L), //
                AlignmentSelectionId.of(4L, 6L), //
                AlignmentSelectionId.of(4L, 7L), //
                AlignmentSelectionId.of(4L, 8L), //
                AlignmentSelectionId.of(3L, 3L), //
                AlignmentSelectionId.of(3L, 4L), //
                AlignmentSelectionId.of(3L, 5L), //
                AlignmentSelectionId.of(8L, 18L), //
                AlignmentSelectionId.of(8L, 19L), //
                AlignmentSelectionId.of(10L, -1L));
    }
}
