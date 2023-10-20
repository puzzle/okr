package ch.puzzle.okr.controller;

import ch.puzzle.okr.mapper.AlignmentSelectionMapper;
import ch.puzzle.okr.models.alignment.AlignmentSelection;
import ch.puzzle.okr.models.alignment.AlignmentSelectionId;
import ch.puzzle.okr.service.business.AlignmentSelectionBusinessService;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(AlignmentController.class)
class AlignmentControllerIT {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private AlignmentSelectionBusinessService alignmentSelectionBusinessService;
    @SpyBean
    private AlignmentSelectionMapper alignmentSelectionMapper;

    static String alignmentObjectiveName = "Objective 5";
    static List<AlignmentSelection> alignmentSelectionPuzzle = List.of(
            AlignmentSelection.Builder.builder().withAlignmentSelectionId(AlignmentSelectionId.of(1L, 20L))
                    .withObjectiveTitle("Objective 1").withKeyResultTitle("KeyResult 20").build(),
            AlignmentSelection.Builder.builder().withAlignmentSelectionId(AlignmentSelectionId.of(1L, 40L))
                    .withObjectiveTitle("Objective 1").withKeyResultTitle("KeyResult 40").build());
    static List<AlignmentSelection> alignmentSelectionOKR = List.of(
            AlignmentSelection.Builder.builder().withAlignmentSelectionId(AlignmentSelectionId.of(5L, 21L))
                    .withObjectiveTitle(alignmentObjectiveName).withKeyResultTitle("KeyResult 21").build(),
            AlignmentSelection.Builder.builder().withAlignmentSelectionId(AlignmentSelectionId.of(5L, 41L))
                    .withObjectiveTitle(alignmentObjectiveName).withKeyResultTitle("KeyResult 41").build(),
            AlignmentSelection.Builder.builder().withAlignmentSelectionId(AlignmentSelectionId.of(5L, 61L))
                    .withObjectiveTitle(alignmentObjectiveName).withKeyResultTitle("KeyResult 61").build(),
            AlignmentSelection.Builder.builder().withAlignmentSelectionId(AlignmentSelectionId.of(5L, 81L))
                    .withObjectiveTitle(alignmentObjectiveName).withKeyResultTitle("KeyResult 81").build());
    static AlignmentSelection alignmentSelectionEmptyKeyResults = AlignmentSelection.Builder.builder()
            .withAlignmentSelectionId(AlignmentSelectionId.of(8L, null)).withObjectiveTitle("Objective 8").build();

    @Test
    void shouldGetAllObjectivesWithKeyResults() throws Exception {
        List<AlignmentSelection> alignmentSelections = new ArrayList<>();
        alignmentSelections.addAll(alignmentSelectionPuzzle);
        alignmentSelections.addAll(alignmentSelectionOKR);
        alignmentSelections.add(alignmentSelectionEmptyKeyResults);
        BDDMockito.given(alignmentSelectionBusinessService.getAlignmentSelectionByQuarterIdAndTeamIdNot(2L, 4L))
                .willReturn(alignmentSelections);

        mvc.perform(get("/api/v2/alignments/selections?quarter=2&team=4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].id", Is.is(1))).andExpect(jsonPath("$[0].keyResults[0].id", Is.is(20)))
                .andExpect(jsonPath("$[0].keyResults[1].id", Is.is(40))).andExpect(jsonPath("$[1].id", Is.is(5)))
                .andExpect(jsonPath("$[1].keyResults[0].id", Is.is(21)))
                .andExpect(jsonPath("$[1].keyResults[1].id", Is.is(41)))
                .andExpect(jsonPath("$[1].keyResults[2].id", Is.is(61)))
                .andExpect(jsonPath("$[1].keyResults[3].id", Is.is(81))).andExpect(jsonPath("$[2].id", Is.is(8)))
                .andExpect(jsonPath("$[2].keyResults.size()", Is.is(0)));
    }

    @Test
    void shouldGetAllObjectivesWithKeyResultsIfAllObjectivesFiltered() throws Exception {
        BDDMockito.given(alignmentSelectionBusinessService.getAlignmentSelectionByQuarterIdAndTeamIdNot(any(), any()))
                .willReturn(Collections.emptyList());

        mvc.perform(get("/api/v2/alignments/selections").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldReturnObjectiveWithEmptyKeyResultListWhenNoKeyResultsInFilteredQuarter() throws Exception {
        BDDMockito.given(alignmentSelectionBusinessService.getAlignmentSelectionByQuarterIdAndTeamIdNot(2L, 4L))
                .willReturn(List.of(alignmentSelectionEmptyKeyResults));

        mvc.perform(get("/api/v2/alignments/selections?quarter=2&team=4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id", Is.is(8))).andExpect(jsonPath("$[0].keyResults.size()", Is.is(0)));
    }
}
