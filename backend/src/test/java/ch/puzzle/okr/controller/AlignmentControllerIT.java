package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.alignment.AlignmentKeyResultDto;
import ch.puzzle.okr.dto.alignment.AlignmentObjectiveDto;
import ch.puzzle.okr.service.AlignmentSelectionService;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(AlignmentController.class)
public class AlignmentControllerIT {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private AlignmentSelectionService alignmentSelectionService;

    static AlignmentObjectiveDto alignmentObjectiveDtoPuzzle = new AlignmentObjectiveDto(1L, "Objective 1",
            List.of(new AlignmentKeyResultDto(20L, "KeyResult 20"), new AlignmentKeyResultDto(40L, "KeyResult 40")));
    static AlignmentObjectiveDto alignmentObjectiveDtoOKR = new AlignmentObjectiveDto(5L, "Objective 5",
            List.of(new AlignmentKeyResultDto(21L, "KeyResult 21"), new AlignmentKeyResultDto(41L, "KeyResult 41"),
                    new AlignmentKeyResultDto(61L, "KeyResult 61"), new AlignmentKeyResultDto(81L, "KeyResult 81")));
    static AlignmentObjectiveDto alignmentObjectiveDtoEmptyKeyResults = new AlignmentObjectiveDto(8L, "Objective 8",
            Collections.emptyList());

    @Test
    void shouldGetAllObjectivesWithKeyResults() throws Exception {
        BDDMockito.given(alignmentSelectionService.getAlignmentSelectionByQuarterIdAndTeamIdNot(2L, 4L)).willReturn(
                List.of(alignmentObjectiveDtoPuzzle, alignmentObjectiveDtoOKR, alignmentObjectiveDtoEmptyKeyResults));

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
        BDDMockito.given(alignmentSelectionService.getAlignmentSelectionByQuarterIdAndTeamIdNot(any(), any()))
                .willReturn(Collections.emptyList());

        mvc.perform(get("/api/v2/alignments/selections").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldReturnObjectiveWithEmptyKeyResultListWhenNoKeyResultsInFilteredQuarter() throws Exception {
        BDDMockito.given(alignmentSelectionService.getAlignmentSelectionByQuarterIdAndTeamIdNot(2L, 4L))
                .willReturn(List.of(alignmentObjectiveDtoEmptyKeyResults));

        mvc.perform(get("/api/v2/alignments/selections?quarter=2&team=4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id", Is.is(8))).andExpect(jsonPath("$[0].keyResults.size()", Is.is(0)));
    }
}
