package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.ObjectiveDto;
import ch.puzzle.okr.dto.OverviewDto;
import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.service.OverviewService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(OverviewController.class)
public class OverviewControllerIT {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private OverviewService overviewService;

    static OverviewDto overviewDtoPuzzle = new OverviewDto(new TeamDto(1L, "Puzzle", 0),
            List.of(new ObjectiveDto(1L, "Objective 1", 1L, "Alice", "Wunderland", 1L, "Puzzle", 1L, "GJ 22/23-Q2",
                    "This is a description", 20L),
                    new ObjectiveDto(2L, "Objective 2", 1L, "Alice", "Wunderland", 1L, "Puzzle", 1L, "GJ 22/23-Q2",
                            "This is a description", 20L)));

    static OverviewDto overviewDtoOKR = new OverviewDto(new TeamDto(2L, "OKR", 0),
            List.of(new ObjectiveDto(5L, "Objective 5", 1L, "Alice", "Wunderland", 2L, "OKR", 1L, "GJ 22/23-Q2",
                    "This is a description", 20L),
                    new ObjectiveDto(7L, "Objective 7", 1L, "Alice", "Wunderland", 2L, "OKR", 1L, "GJ 22/23-Q2",
                            "This is a description", 20L)));

    static OverviewDto overviewDtoKuchen = new OverviewDto(new TeamDto(3L, "Kuchen", 0), List.of(new ObjectiveDto(8L,
            "Objective 8", 1L, "Alice", "Wunderland", 3L, "Kuchen", 1L, "GJ 22/23-Q2", "This is a description", 20L)));

    static OverviewDto overviewDtoFindus = new OverviewDto(new TeamDto(4L, "Findus", 0), Collections.emptyList());
}
