package ch.puzzle.okr.controller;

import static ch.puzzle.okr.Constants.BACK_LOG_QUARTER_LABEL;
import static ch.puzzle.okr.test.TestConstants.BACK_LOG_QUARTER_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
    static Quarter quarter2 = Quarter.Builder
            .builder()
            .withId(2L)
            .withLabel("GJ 22/23-Q3")
            .withStartDate(LocalDate.of(2023, 1, 1))
            .withEndDate(LocalDate.of(2023, 3, 31))
            .build();
    static Quarter backlogQuarter = Quarter.Builder
            .builder()
            .withId(BACK_LOG_QUARTER_ID)
            .withLabel(BACK_LOG_QUARTER_LABEL)
            .withStartDate(null)
            .withEndDate(null)
            .build();
    static List<Quarter> quaterList = Arrays.asList(quarter1, quarter2, backlogQuarter);

    @Autowired
    private MockMvc mvc;
    @MockBean
    private QuarterBusinessService quarterBusinessService;

    @DisplayName("Should get all quarters")
    @Test
    void shouldGetAllQuarters() throws Exception {
        BDDMockito.given(quarterBusinessService.getQuarters()).willReturn(quaterList);

        mvc
                .perform(get("/api/v2/quarters").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].id", Is.is(1)))
                .andExpect(jsonPath("$[0].label", Is.is("GJ 22/23-Q2")))
                .andExpect(jsonPath("$[0].startDate", Is.is(LocalDate.of(2022, 9, 1).toString())))
                .andExpect(jsonPath("$[0].endDate", Is.is(LocalDate.of(2022, 12, 31).toString())))
                .andExpect(jsonPath("$[1].id", Is.is(2)))
                .andExpect(jsonPath("$[1].label", Is.is("GJ 22/23-Q3")))
                .andExpect(jsonPath("$[1].startDate", Is.is(LocalDate.of(2023, 1, 1).toString())))
                .andExpect(jsonPath("$[1].endDate", Is.is(LocalDate.of(2023, 3, 31).toString())))
                .andExpect(jsonPath("$[2].id", Is.is((int) BACK_LOG_QUARTER_ID)))
                .andExpect(jsonPath("$[2].label", Is.is(BACK_LOG_QUARTER_LABEL)));
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
