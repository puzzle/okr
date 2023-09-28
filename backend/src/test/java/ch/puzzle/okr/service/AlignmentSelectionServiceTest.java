package ch.puzzle.okr.service;

import ch.puzzle.okr.dto.alignment.AlignmentObjectiveDto;
import ch.puzzle.okr.mapper.AlignmentSelectionMapper;
import ch.puzzle.okr.models.AlignmentSelection;
import ch.puzzle.okr.models.AlignmentSelectionId;
import ch.puzzle.okr.service.business.AlignmentSelectionBusinessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AlignmentSelectionServiceTest {
    @InjectMocks
    AlignmentSelectionService alignmentSelectionService;
    @Mock
    AlignmentSelectionMapper alignmentSelectionMapper;
    @Mock
    AlignmentSelectionBusinessService alignmentSelectionBusinessService;

    private AlignmentSelection createSelection() {
        return AlignmentSelection.Builder.builder().withAlignmentSelectionId(AlignmentSelectionId.of(10L, null))
                .withQuarterId(2L).withTeamId(3L).withObjectiveTitle("Lorem ipsum est").build();
    }

    private AlignmentObjectiveDto createSelectionDto() {
        return new AlignmentObjectiveDto(10L, "Lorem ipsum est", List.of());
    }

    @Test
    void getAlignmentSelectionByQuarterIdAndTeamIdNot_ShouldReturnObjective() {
        List<AlignmentSelection> selections = List.of(createSelection());
        Mockito.when(alignmentSelectionBusinessService.getAlignmentSelectionByQuarterIdAndTeamIdNot(2L, 4L))
                .thenReturn(selections);
        Mockito.when(alignmentSelectionMapper.toDto(selections)).thenReturn(List.of(createSelectionDto()));

        List<AlignmentObjectiveDto> alignmentObjectiveDtos = alignmentSelectionService
                .getAlignmentSelectionByQuarterIdAndTeamIdNot(2L, 4L);

        assertEquals(1, alignmentObjectiveDtos.size());
        assertEquals(10L, alignmentObjectiveDtos.get(0).id());
    }
}
