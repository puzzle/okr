package ch.puzzle.okr.controller;

import static ch.puzzle.okr.test.EvaluationViewTestHelper.*;

import ch.puzzle.okr.dto.EvaluationDto;
import ch.puzzle.okr.mapper.EvaluationViewMapper;
import ch.puzzle.okr.models.evaluation.EvaluationView;
import ch.puzzle.okr.service.business.EvaluationViewBusinessService;
import java.util.List;

import ch.puzzle.okr.util.TeamQuarterFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

@WebMvcTest(EvaluationViewController.class)
@WithMockUser(value = "spring")
class EvaluationViewControllerIT {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private EvaluationViewMapper evaluationViewMapper;

    @MockitoBean
    private EvaluationViewBusinessService evaluationViewBusinessService;

    @DisplayName("Should return evaluation data for valid team and quarter parameters")
    @Test
    void shouldReturnEvaluationData() throws Exception {
        List<Long> teamIds = List.of(1L, 2L);
        Long quarterId = 3L;
        TeamQuarterFilter teamQuarterFilter = new TeamQuarterFilter(teamIds, quarterId);
        // Dummy object to simulate the internal workflow
        List<EvaluationView> evaluationViews = generateEvaluationViews(teamQuarterFilter);
        // Create a dummy EvaluationDto with sample data
        EvaluationDto evaluationDto = generateEvaluationDto();

        // Define mock behavior
        BDDMockito.given(evaluationViewMapper.fromDto(teamIds, quarterId)).willReturn(teamQuarterFilter);
        BDDMockito.given(evaluationViewBusinessService.findByIds(teamQuarterFilter)).willReturn(evaluationViews);
        BDDMockito.given(evaluationViewMapper.toDto(evaluationViews)).willReturn(evaluationDto);

        // Perform GET request and assert the JSON response
        ResultActions resultActions = mvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v2/evaluation")
                        .param("team", teamIds.stream().map(Object::toString).toArray(String[]::new))
                        .param("quarter", quarterId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        validateResponse(resultActions, evaluationDto);
    }

    @DisplayName("Should return 400 Bad Request if required parameters are missing")
    @Test
    void shouldReturnBadRequestWhenParametersMissing() throws Exception {
        mvc
                .perform(MockMvcRequestBuilders.get("/api/v2/evaluation").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("Should return 400 Bad Request if parameter quarter is missing")
    @Test
    void shouldReturnBadRequestWhenParameterQuarterIsMissing() throws Exception {
        List<Long> teamIds = List.of(1L, 2L);

        mvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v2/evaluation")
                        .param("team", teamIds.stream().map(Object::toString).toArray(String[]::new))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("Should return 400 Bad Request if parameter team is missing")
    @Test
    void shouldReturnBadRequestWhenParameterTeamIsMissing() throws Exception {

        mvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v2/evaluation")
                        .param("quarter", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("Should return 404 Not Found when provided ids are not found")
    @Test
    void shouldReturnNotFoundForNonExistentIds() throws Exception {
        List<Long> teamIds = List.of(999L);
        Long quarterId = 888L;

        TeamQuarterFilter teamQuarterFilter = new TeamQuarterFilter(teamIds, quarterId);

        BDDMockito.given(evaluationViewMapper.fromDto(teamIds, quarterId)).willReturn(teamQuarterFilter);
        // Simulate not found by throwing an exception
        BDDMockito
                .given(evaluationViewBusinessService.findByIds(teamQuarterFilter))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v2/evaluation")
                        .param("team", "999")
                        .param("quarter", "888")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("Should return 401 Unauthorized when user is not authenticated")
    void shouldReturnUnauthorizedForUnauthenticated() throws Exception {
        List<Long> teamIds = List.of(999L);
        Long quarterId = 888L;

        TeamQuarterFilter teamQuarterFilter = new TeamQuarterFilter(teamIds, quarterId);

        BDDMockito.given(evaluationViewMapper.fromDto(teamIds, quarterId)).willReturn(teamQuarterFilter);
        BDDMockito
                .given(evaluationViewBusinessService.findByIds(teamQuarterFilter))
                .willThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        mvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v2/evaluation")
                        .param("team", "1")
                        .param("quarter", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    private void validateResponse(ResultActions response, EvaluationDto dto) throws Exception {
        response
                .andExpect(MockMvcResultMatchers.jsonPath("$.objectiveAmount").value(dto.objectiveAmount()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.completedObjectivesAmount")
                        .value(dto.completedObjectivesAmount()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.successfullyCompletedObjectivesAmount")
                        .value(dto.successfullyCompletedObjectivesAmount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.keyResultAmount").value(dto.keyResultAmount()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.keyResultsOrdinalAmount")
                        .value(dto.keyResultsOrdinalAmount()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.keyResultsMetricAmount")
                        .value(dto.keyResultsMetricAmount()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.keyResultsInTargetOrStretchAmount")
                        .value(dto.keyResultsInTargetOrStretchAmount()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.keyResultsInFailAmount")
                        .value(dto.keyResultsInFailAmount()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.keyResultsInCommitAmount")
                        .value(dto.keyResultsInCommitAmount()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.keyResultsInTargetAmount")
                        .value(dto.keyResultsInTargetAmount()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.keyResultsInStretchAmount")
                        .value(dto.keyResultsInStretchAmount()));
    }
}