package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.alignment.AlignmentConnectionDto;
import ch.puzzle.okr.dto.alignment.AlignmentLists;
import ch.puzzle.okr.dto.alignment.AlignmentObjectDto;
import ch.puzzle.okr.service.business.AlignmentBusinessService;
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

import java.util.List;

import static ch.puzzle.okr.TestConstants.TEAM_PUZZLE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(AlignmentController.class)
class AlignmentControllerIT {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private AlignmentBusinessService alignmentBusinessService;

    private static final String OBJECTIVE = "objective";
    private static final String ONGOING = "ONGOING";
    static AlignmentObjectDto alignmentObjectDto1 = new AlignmentObjectDto(3L, "Title of first Objective", TEAM_PUZZLE,
            ONGOING, OBJECTIVE);
    static AlignmentObjectDto alignmentObjectDto2 = new AlignmentObjectDto(4L, "Title of second Objective", "BBT",
            ONGOING, OBJECTIVE);
    static AlignmentConnectionDto alignmentConnectionDto = new AlignmentConnectionDto(4L, 3L, null);

    static AlignmentLists alignmentLists = new AlignmentLists(List.of(alignmentObjectDto1, alignmentObjectDto2),
            List.of(alignmentConnectionDto));

    @Test
    void shouldReturnCorrectAlignmentData() throws Exception {
        BDDMockito.given(alignmentBusinessService.getAlignmentListsByFilters(2L, List.of(4L, 5L, 8L), ""))
                .willReturn(alignmentLists);

        mvc.perform(get("/api/v2/alignments/alignmentLists?quarterFilter=2&teamFilter=4,5,8")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.alignmentObjectDtoList", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.alignmentObjectDtoList[1].objectId", Is.is(4)))
                .andExpect(jsonPath("$.alignmentConnectionDtoList[0].alignedObjectiveId", Is.is(4)))
                .andExpect(jsonPath("$.alignmentConnectionDtoList[0].targetObjectiveId", Is.is(3)));
    }

    @Test
    void shouldReturnCorrectAlignmentDataWithObjectiveSearch() throws Exception {
        BDDMockito.given(alignmentBusinessService.getAlignmentListsByFilters(2L, List.of(4L, 5L, 8L), "secon"))
                .willReturn(alignmentLists);

        mvc.perform(get("/api/v2/alignments/alignmentLists?quarterFilter=2&teamFilter=4,5,8&objectiveQuery=secon")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.alignmentObjectDtoList", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.alignmentObjectDtoList[1].objectId", Is.is(4)))
                .andExpect(jsonPath("$.alignmentConnectionDtoList[0].alignedObjectiveId", Is.is(4)))
                .andExpect(jsonPath("$.alignmentConnectionDtoList[0].targetObjectiveId", Is.is(3)));
    }
}
