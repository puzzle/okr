package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.objectives.GetObjectiveDTO;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration
@WebAppConfiguration
@SpringBootTest
@TestConfiguration
class ObjectiveControllerTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        //Setup Objectmapper to parse mvcResult in tests
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        objectMapper.registerModule(new JavaTimeModule());

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .dispatchOptions(true)
                .build();
    }

    @Test
    void checkIfHTTPMethodMatches() throws Exception {
        this.mockMvc.perform(get("/api/v1/objectives"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void checkFalseHTTPMethod() throws Exception {
        this.mockMvc.perform(post("/api/v1/objectives"))
                .andExpect(status().isMethodNotAllowed()).andReturn();
    }

    @Test
    void checkControllerResult() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        MvcResult mvcResult = this.mockMvc.perform(get("/api/v1/objectives"))
                .andExpect(status().isOk()).andReturn();

        List<GetObjectiveDTO> dtos = this.objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
                });

        assertEquals(3, dtos.size());
        assertEquals("This is the description of Objective 1", dtos.get(0).getDescription());
        assertEquals(LocalDateTime.parse("2020-01-01T00:00", formatter), dtos.get(2).getCreatedOn());
    }
}
