package ch.puzzle.okr.controller;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.service.QuarterService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(QuarterController.class)
class QuarterControllerIT {

    static Quarter quarter1 = Quarter.Builder.builder().withId(1L).withLabel("GJ 22/23-Q2").build();
    static Quarter quarter2 = Quarter.Builder.builder().withId(2L).withLabel("GJ 22/23-Q3").build();
    static List<Quarter> quaterList = Arrays.asList(quarter1, quarter2);

    @Autowired
    private MockMvc mvc;
    @MockBean
    private QuarterService quarterService;

    @Test
    void shouldGetAllQuarters() throws Exception {
        BDDMockito.given(quarterService.getOrCreateQuarters()).willReturn(quaterList);

        mvc.perform(get("/api/v1/quarters").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Is.is(1))).andExpect(jsonPath("$[0].label", Is.is("GJ 22/23-Q2")))
                .andExpect(jsonPath("$[1].id", Is.is(2))).andExpect(jsonPath("$[1].label", Is.is("GJ 22/23-Q3")));
    }

    @Test
    void shouldGetAllTeamsIfNoTeamsExists() throws Exception {
        BDDMockito.given(quarterService.getOrCreateQuarters()).willReturn(Collections.emptyList());

        mvc.perform(get("/api/v1/quarters").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(0)));
    }
}