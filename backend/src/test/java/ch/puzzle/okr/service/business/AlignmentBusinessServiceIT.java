package ch.puzzle.okr.service.business;

import ch.puzzle.okr.dto.alignment.AlignmentLists;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringIntegrationTest
class AlignmentBusinessServiceIT {
    @Autowired
    private AlignmentBusinessService alignmentBusinessService;

    @Test
    void shouldReturnCorrectAlignmentData() {
        AlignmentLists alignmentLists = alignmentBusinessService.getAlignmentsByFilters(9L, List.of(5L, 6L), "");

        assertEquals(6, alignmentLists.alignmentObjectDtoList().size());
        assertEquals(4, alignmentLists.alignmentConnectionDtoList().size());
        assertEquals(40L, alignmentLists.alignmentObjectDtoList().get(0).objectId());
        assertEquals("objective", alignmentLists.alignmentObjectDtoList().get(0).objectType());
        assertEquals(40L, alignmentLists.alignmentObjectDtoList().get(1).objectId());
        assertEquals("keyResult", alignmentLists.alignmentObjectDtoList().get(1).objectType());
        assertEquals(41L, alignmentLists.alignmentObjectDtoList().get(2).objectId());
        assertEquals(43L, alignmentLists.alignmentObjectDtoList().get(3).objectId());
        assertEquals(44L, alignmentLists.alignmentObjectDtoList().get(4).objectId());
        assertEquals(42L, alignmentLists.alignmentObjectDtoList().get(5).objectId());
        assertEquals(41L, alignmentLists.alignmentConnectionDtoList().get(0).alignedObjectiveId());
        assertEquals(40L, alignmentLists.alignmentConnectionDtoList().get(0).targetObjectiveId());
        assertNull(alignmentLists.alignmentConnectionDtoList().get(0).targetKeyResultId());
        assertEquals(43L, alignmentLists.alignmentConnectionDtoList().get(1).alignedObjectiveId());
        assertEquals(40L, alignmentLists.alignmentConnectionDtoList().get(1).targetKeyResultId());
        assertNull(alignmentLists.alignmentConnectionDtoList().get(1).targetObjectiveId());
        assertEquals(44L, alignmentLists.alignmentConnectionDtoList().get(2).alignedObjectiveId());
        assertEquals(42L, alignmentLists.alignmentConnectionDtoList().get(2).targetObjectiveId());
        assertNull(alignmentLists.alignmentConnectionDtoList().get(2).targetKeyResultId());
        assertEquals(42L, alignmentLists.alignmentConnectionDtoList().get(3).alignedObjectiveId());
        assertEquals(40L, alignmentLists.alignmentConnectionDtoList().get(3).targetObjectiveId());
        assertNull(alignmentLists.alignmentConnectionDtoList().get(3).targetKeyResultId());
    }

    @Test
    void shouldReturnCorrectAlignmentDataWhenLimitedTeamMatching() {
        AlignmentLists alignmentLists = alignmentBusinessService.getAlignmentsByFilters(9L, List.of(6L), "");

        assertEquals(2, alignmentLists.alignmentObjectDtoList().size());
        assertEquals(1, alignmentLists.alignmentConnectionDtoList().size());
        assertEquals(44L, alignmentLists.alignmentObjectDtoList().get(0).objectId());
        assertEquals("objective", alignmentLists.alignmentObjectDtoList().get(0).objectType());
        assertEquals(42L, alignmentLists.alignmentObjectDtoList().get(1).objectId());
        assertEquals("objective", alignmentLists.alignmentObjectDtoList().get(1).objectType());
        assertEquals(44L, alignmentLists.alignmentConnectionDtoList().get(0).alignedObjectiveId());
        assertEquals(42L, alignmentLists.alignmentConnectionDtoList().get(0).targetObjectiveId());
        assertNull(alignmentLists.alignmentConnectionDtoList().get(0).targetKeyResultId());
    }

    @Test
    void shouldReturnCorrectAlignmentDataWithObjectiveSearch() {
        AlignmentLists alignmentLists = alignmentBusinessService.getAlignmentsByFilters(9L, List.of(4L, 5L, 6L, 8L),
                "lehrling");

        assertEquals(3, alignmentLists.alignmentObjectDtoList().size());
        assertEquals(2, alignmentLists.alignmentConnectionDtoList().size());
        assertEquals(42L, alignmentLists.alignmentObjectDtoList().get(0).objectId());
        assertEquals("objective", alignmentLists.alignmentObjectDtoList().get(0).objectType());
        assertEquals(40L, alignmentLists.alignmentObjectDtoList().get(1).objectId());
        assertEquals("objective", alignmentLists.alignmentObjectDtoList().get(1).objectType());
        assertEquals(44L, alignmentLists.alignmentObjectDtoList().get(2).objectId());
        assertEquals("objective", alignmentLists.alignmentObjectDtoList().get(2).objectType());
        assertEquals(42L, alignmentLists.alignmentConnectionDtoList().get(0).alignedObjectiveId());
        assertEquals(40L, alignmentLists.alignmentConnectionDtoList().get(0).targetObjectiveId());
        assertNull(alignmentLists.alignmentConnectionDtoList().get(0).targetKeyResultId());
        assertEquals(44L, alignmentLists.alignmentConnectionDtoList().get(1).alignedObjectiveId());
        assertEquals(42L, alignmentLists.alignmentConnectionDtoList().get(1).targetObjectiveId());
        assertNull(alignmentLists.alignmentConnectionDtoList().get(1).targetKeyResultId());
    }

    @Test
    void shouldReturnCorrectAlignmentDataWithKeyResultWhenMatchingObjectiveSearch() {
        AlignmentLists alignmentLists = alignmentBusinessService.getAlignmentsByFilters(9L, List.of(4L, 5L, 6L, 8L),
                "firmenums");

        assertEquals(2, alignmentLists.alignmentObjectDtoList().size());
        assertEquals(1, alignmentLists.alignmentConnectionDtoList().size());
        assertEquals(43L, alignmentLists.alignmentObjectDtoList().get(0).objectId());
        assertEquals("objective", alignmentLists.alignmentObjectDtoList().get(0).objectType());
        assertEquals(40L, alignmentLists.alignmentObjectDtoList().get(1).objectId());
        assertEquals("keyResult", alignmentLists.alignmentObjectDtoList().get(1).objectType());
        assertEquals(43L, alignmentLists.alignmentConnectionDtoList().get(0).alignedObjectiveId());
        assertEquals(40L, alignmentLists.alignmentConnectionDtoList().get(0).targetKeyResultId());
        assertNull(alignmentLists.alignmentConnectionDtoList().get(0).targetObjectiveId());
    }

    @Test
    void shouldReturnEmptyAlignmentDataWhenAlignments() {
        AlignmentLists alignmentLists = alignmentBusinessService.getAlignmentsByFilters(3L, List.of(5L, 6L), "");

        assertEquals(0, alignmentLists.alignmentObjectDtoList().size());
        assertEquals(0, alignmentLists.alignmentConnectionDtoList().size());
    }

    @Test
    void shouldReturnEmptyAlignmentDataWhenNoMatchingObjectiveSearch() {
        AlignmentLists alignmentLists = alignmentBusinessService.getAlignmentsByFilters(9L, List.of(4L, 5L, 6L, 8L),
                "spass");

        assertEquals(0, alignmentLists.alignmentObjectDtoList().size());
        assertEquals(0, alignmentLists.alignmentConnectionDtoList().size());
    }

    @Test
    void shouldReturnEmptyAlignmentDataWhenNoMatchingQuarterFilter() {
        AlignmentLists alignmentLists = alignmentBusinessService.getAlignmentsByFilters(311L, List.of(4L, 5L, 6L, 8L),
                "");

        assertEquals(0, alignmentLists.alignmentObjectDtoList().size());
        assertEquals(0, alignmentLists.alignmentConnectionDtoList().size());
    }
}
