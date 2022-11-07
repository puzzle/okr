package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.mapper.KeyResultMapper;
import ch.puzzle.okr.models.ExpectedEvolution;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.service.KeyResultService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(KeyResultController.class)
public class KeyResultControllerIT {
    static KeyResult keyResult = KeyResult.Builder.builder().withId(5L).withTitle("Keyresult 1").build();
    static KeyResultDto keyresultDto = new KeyResultDto(5L, 1L, "Keyresult 1", "Description", 1L, "Alice", "Wunderland", 1L, 1, 2022, ExpectedEvolution.CONSTANT, Unit.PERCENT, 20L, 100L);

    @Autowired
    private MockMvc mvc;
    @MockBean
    private KeyResultService keyResultService;
    @MockBean
    private KeyResultMapper keyResultMapper;

    @BeforeEach
    void setUp() {
        // setup keyresult mapper
        BDDMockito.given(keyResultMapper.toKeyResult(keyresultDto)).willReturn(keyResult);
    }

    @Test
    void shouldReturnUpdatedKeyresult() throws Exception {
        KeyResult keyResult = KeyResult.Builder.builder()
                .withId(1L)
                .withTitle("Updated Keyresult 1")
                .build();
        BDDMockito.given(keyResultService.updateKeyResult(any())).willReturn(keyResult);

        mvc.perform(put("/api/v1/keyresults/1").contentType(MediaType.APPLICATION_JSON).content("{\"id\":  1, \"title\":  \"Updated Keyresult 1\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Is.is(1)))
                .andExpect(jsonPath("$.title", Is.is("Updated Keyresult 1")));
    }

    @Test
    void shouldReturnNotFound() throws Exception {
        BDDMockito.given(keyResultService.updateKeyResult(any()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        any()));

        mvc.perform(put("/api/v1/keyresults/10").contentType(MediaType.APPLICATION_JSON).content("{\"id\":  1, \"title\":  \"Updated Keyresult 1\"}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
