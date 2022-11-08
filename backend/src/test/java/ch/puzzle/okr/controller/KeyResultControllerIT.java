package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.mapper.KeyResultMapper;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.service.KeyResultService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class KeyResultControllerIT {
    @MockBean
    KeyResultRepository keyResultRepository;

    @MockBean
    KeyResultMapper keyResultMapper;

    @MockBean
    KeyResultService keyResultService;

    @Autowired
    private MockMvc mvc;
    private KeyResultDto keyResultDTO;
    private User user;
    private Objective objective;
    private Quarter quarter;
    private KeyResult keyResult;

    @BeforeEach
    void setUp() {
        this.keyResultDTO = new KeyResultDto(5L, 5L, "", "", 5L, "", "", 5L, 2, 2022, ExpectedEvolution.INCREASE, Unit.PERCENT, 0L, 1L);
        BDDMockito.given(keyResultMapper.toKeyResult(keyResultDTO)).willReturn(keyResult);

        this.user = User.Builder.builder()
                .withId(1L)
                .withEmail("newMail@tese.com")
                .build();

        this.objective = Objective.Builder.builder()
                .withId(5L)
                .withTitle("Objective 1")
                .build();

        this.quarter = Quarter.Builder.builder()
                .withId(5L)
                .withNumber(2)
                .withYear(2022)
                .build();

        this.keyResult = KeyResult.Builder.builder()
                .withId(5L)
                .withTitle("test")
                .withObjective(this.objective)
                .withOwner(this.user)
                .withQuarter(this.quarter)
                .build();
    }

    @Test
    void shouldReturnUpdatedKeyresult() throws Exception {
        KeyResult keyResult = KeyResult.Builder.builder()
                .withId(1L)
                .withTitle("Updated Keyresult 1")
                .build();
        BDDMockito.given(keyResultService.updateKeyResult(any())).willReturn(keyResult);

        mvc.perform(put("/api/v1/keyresults/1").contentType(MediaType.APPLICATION_JSON).content("{\"title\":  \"Updated Keyresult 1\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Is.is(1)))
                .andExpect(jsonPath("$.title", Is.is("Updated Keyresult 1")));
    }

    @Test
    void shouldReturnNotFound() throws Exception {
        BDDMockito.given(keyResultService.updateKeyResult(any()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        any()));
        mvc.perform(put("/api/v1/keyresults/10").contentType(MediaType.APPLICATION_JSON).content("{\"title\":  \"Updated Keyresult 1\"}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }
        void createKeyResult() throws Exception {
            BDDMockito.given(this.keyResultService.getQuarterById(5)).willReturn(this.quarter);
            BDDMockito.given(this.keyResultService.getOwnerById(5)).willReturn(this.user);
            BDDMockito.given(this.keyResultService.getObjectivebyId(5)).willReturn(this.objective);
            BDDMockito.given(this.keyResultService.createKeyResult(any())).willReturn(this.keyResult);

            ObjectMapper mapper = new ObjectMapper();
            mvc.perform(post("/api/v1/keyresults")
                            .content(mapper.writeValueAsString(this.keyResultDTO))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.id", Is.is(5)));

        }

}
