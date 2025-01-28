package ch.puzzle.okr.controller;

import static ch.puzzle.okr.Constants.BACK_LOG_QUARTER_LABEL;
import static ch.puzzle.okr.test.TestConstants.BACK_LOG_QUARTER_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import ch.puzzle.okr.dto.QuarterDto;
import ch.puzzle.okr.mapper.QuarterMapper;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.service.business.QuarterBusinessService;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(QuarterController.class)
class QuarterControllerIT {

    static Quarter quarter1 = Quarter.Builder
            .builder()
            .withId(1L)
            .withLabel("GJ 22/23-Q2")
            .withStartDate(LocalDate.of(2022, 9, 1))
            .withEndDate(LocalDate.of(2022, 12, 31))
            .build();

    static QuarterDto quarter1Dto = new QuarterDto(quarter1.getId(), quarter1.getLabel(), quarter1.getStartDate(), quarter1.getEndDate(), quarter1.isBacklogQuarter());

    static Quarter quarter2 = Quarter.Builder
            .builder()
            .withId(2L)
            .withLabel("GJ 22/23-Q3")
            .withStartDate(LocalDate.of(2023, 1, 1))
            .withEndDate(LocalDate.of(2023, 3, 31))
            .build();

    static QuarterDto quarter2Dto = new QuarterDto(quarter2.getId(), quarter2.getLabel(), quarter2.getStartDate(), quarter2.getEndDate(), quarter2.isBacklogQuarter());

    static Quarter backlogQuarter = Quarter.Builder
            .builder()
            .withId(BACK_LOG_QUARTER_ID)
            .withLabel(BACK_LOG_QUARTER_LABEL)
            .withStartDate(null)
            .withEndDate(null)
            .build();

    static QuarterDto backlogQuarterDto = new QuarterDto(backlogQuarter.getId(), backlogQuarter.getLabel(), backlogQuarter.getStartDate(), backlogQuarter.getEndDate(), backlogQuarter.isBacklogQuarter());

    static List<Quarter> quarterList = Arrays.asList(quarter1, quarter2, backlogQuarter);

    static List<QuarterDto> quarterDtoList = Arrays.asList(quarter1Dto, quarter2Dto, backlogQuarterDto);

    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private QuarterBusinessService quarterBusinessService;
    @MockitoBean
    private QuarterMapper quarterMapper;

    @DisplayName("Should get all quarters")
    @Test
    void shouldGetAllQuarters() throws Exception {
        BDDMockito.given(quarterBusinessService.getQuarters()).willReturn(quarterList);
        BDDMockito.given(quarterMapper.toDtos(any())).willReturn(quarterDtoList);

        mvc
                .perform(get("/api/v2/quarters").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].id", Is.is(1)))
                .andExpect(jsonPath("$[0].label", Is.is("GJ 22/23-Q2")))
                .andExpect(jsonPath("$[0].startDate", Is.is(LocalDate.of(2022, 9, 1).toString())))
                .andExpect(jsonPath("$[0].endDate", Is.is(LocalDate.of(2022, 12, 31).toString())))
                .andExpect(jsonPath("$[0].isBacklogQuarter", Is.is(false)))
                .andExpect(jsonPath("$[1].id", Is.is(2)))
                .andExpect(jsonPath("$[1].label", Is.is("GJ 22/23-Q3")))
                .andExpect(jsonPath("$[1].startDate", Is.is(LocalDate.of(2023, 1, 1).toString())))
                .andExpect(jsonPath("$[1].endDate", Is.is(LocalDate.of(2023, 3, 31).toString())))
                .andExpect(jsonPath("$[1].isBacklogQuarter", Is.is(false)))
                .andExpect(jsonPath("$[2].id", Is.is((int) BACK_LOG_QUARTER_ID)))
                .andExpect(jsonPath("$[2].label", Is.is(BACK_LOG_QUARTER_LABEL)))
                .andExpect(jsonPath("$[2].isBacklogQuarter", Is.is(true)));
    }

    @DisplayName("Should return an empty list if no quarters exist")
    @Test
    void shouldReturnEmptyListIfNoQuarterExists() throws Exception {
        BDDMockito.given(quarterBusinessService.getQuarters()).willReturn(Collections.emptyList());

        mvc
                .perform(get("/api/v2/quarters").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @DisplayName("Should call current-quarter after requesting so")
    @Test
    void shouldCallCurrentQuarterAfterRequest() throws Exception {
        mvc.perform(get("/api/v2/quarters/current").contentType(MediaType.APPLICATION_JSON));

        BDDMockito.verify(quarterBusinessService, Mockito.times(1)).getCurrentQuarter();
    }
}
