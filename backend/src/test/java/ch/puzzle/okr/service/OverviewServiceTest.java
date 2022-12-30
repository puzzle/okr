package ch.puzzle.okr.service;

import ch.puzzle.okr.dto.ObjectiveDto;
import ch.puzzle.okr.dto.OverviewDto;
import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.mapper.OverviewMapper;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.repository.ObjectiveRepository;
import ch.puzzle.okr.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OverviewServiceTest {
    @MockBean
    OverviewMapper overviewMapper = Mockito.mock(OverviewMapper.class);
    @MockBean
    TeamRepository teamRepository = Mockito.mock(TeamRepository.class);
    @MockBean
    ObjectiveRepository objectiveRepository = Mockito.mock(ObjectiveRepository.class);

    OverviewDto overview1;
    OverviewDto overview2;
    OverviewDto overview3;
    Objective objective1;
    Objective objective2;
    Objective objective3;

    Team team1;
    Team team2;
    Team team3;

    List<Objective> objectiveList;
    List<Team> teamList;
    List<OverviewDto> overviewList;

    @InjectMocks
    OverviewService overviewService;

    @BeforeEach
    void setUp() {
        this.objective1 = Objective.Builder.builder().withId(1L).withTitle("Objective 1")
                .withQuarter(Quarter.Builder.builder().withId(1L).build())
                .withTeam(Team.Builder.builder().withId(1L).build()).build();
        this.objective2 = Objective.Builder.builder().withId(2L).withTitle("Objective 2")
                .withQuarter(Quarter.Builder.builder().withId(2L).build())
                .withTeam(Team.Builder.builder().withId(1L).build()).build();
        this.objective3 = Objective.Builder.builder().withId(3L).withTitle("Objective 3")
                .withQuarter(Quarter.Builder.builder().withId(1L).build())
                .withTeam(Team.Builder.builder().withId(2L).build()).build();
        this.objectiveList = List.of(objective1, objective2, objective3);
        this.team1 = Team.Builder.builder().withId(1L).withName("Puzzle ITC").build();
        this.team2 = Team.Builder.builder().withId(2L).withName("B.Team 2").build();
        this.team3 = Team.Builder.builder().withId(3L).withName("A.Team 3").build();
        this.overview1 = new OverviewDto(new TeamDto(team1.getId(), team1.getName()),
                List.of(new ObjectiveDto(objective1.getId(), objective1.getTitle(), null, null, null, null, null,
                        objective1.getQuarter().getId(), null, null, null),
                        new ObjectiveDto(objective2.getId(), objective2.getTitle(), null, null, null, null, null,
                                objective2.getQuarter().getId(), null, null, null)));
        this.overview2 = new OverviewDto(new TeamDto(team2.getId(), team2.getName()),
                List.of(new ObjectiveDto(objective3.getId(), objective3.getTitle(), null, null, null, null, null,
                        objective3.getQuarter().getId(), null, null, null)));
        this.overview3 = new OverviewDto(new TeamDto(team3.getId(), team3.getName()), Collections.emptyList());
        this.teamList = List.of(team1, team2, team3);
        this.overviewList = List.of(overview1, overview2, overview3);
    }

    @Test
    void shouldGetAllTeamsWithAllObjectives() {
        when(teamRepository.findAllById(Collections.emptyList())).thenReturn(teamList);
        when(teamRepository.findByName("Puzzle ITC")).thenReturn(team1);
        when(objectiveRepository.findByQuarterIdAndTeamId(null, 1L)).thenReturn(List.of(objective1, objective2));
        when(objectiveRepository.findByQuarterIdAndTeamId(null, 2L)).thenReturn(List.of(objective3));
        when(objectiveRepository.findByQuarterIdAndTeamId(null, 3L)).thenReturn(Collections.emptyList());
        when(overviewMapper.toDto(team1, List.of(objective1, objective2))).thenReturn(overview1);
        when(overviewMapper.toDto(team2, List.of(objective3))).thenReturn(overview2);
        when(overviewMapper.toDto(team3, Collections.emptyList())).thenReturn(overview3);

        List<OverviewDto> overview = overviewService.getOverview(Collections.emptyList(), null);

        assertEquals(3, overview.size());
        assertEquals(1, overview.get(0).getTeam().getId());
        assertEquals(2, overview.get(0).getObjectives().size());
        assertEquals(3, overview.get(1).getTeam().getId());
        assertEquals(0, overview.get(1).getObjectives().size());
        assertEquals(2, overview.get(2).getTeam().getId());
        assertEquals(1, overview.get(2).getObjectives().size());
    }

    @Test
    void shouldFilterTeamsWithTeamsFilter() {
        when(teamRepository.findAllById(List.of(1L, 3L))).thenReturn(List.of(team1, team3));
        when(teamRepository.findByName("Puzzle ITC")).thenReturn(team1);
        when(objectiveRepository.findByQuarterIdAndTeamId(null, 1L)).thenReturn(List.of(objective1, objective2));
        when(objectiveRepository.findByQuarterIdAndTeamId(null, 3L)).thenReturn(Collections.emptyList());
        when(overviewMapper.toDto(team1, List.of(objective1, objective2))).thenReturn(overview1);
        when(overviewMapper.toDto(team3, Collections.emptyList())).thenReturn(overview3);

        List<OverviewDto> overview = overviewService.getOverview(List.of(1L, 3L), null);

        assertEquals(2, overview.size());
        assertEquals(1, overview.get(0).getTeam().getId());
        assertEquals(2, overview.get(0).getObjectives().size());
        assertEquals(3, overview.get(1).getTeam().getId());
        assertEquals(0, overview.get(1).getObjectives().size());
    }

    @Test
    void shouldFilterQuarterWithQuarterFilter() {
        OverviewDto overview1WithoutObjective2 = new OverviewDto(new TeamDto(team1.getId(), team1.getName()),
                List.of(new ObjectiveDto(objective1.getId(), objective1.getTitle(), null, null, null, null, null,
                        objective1.getQuarter().getId(), null, null, null)));
        when(teamRepository.findByName("Puzzle ITC")).thenReturn(team1);
        when(teamRepository.findAllById(Collections.emptyList())).thenReturn(teamList);
        when(objectiveRepository.findByQuarterIdAndTeamId(1L, 1L)).thenReturn(List.of(objective1));
        when(objectiveRepository.findByQuarterIdAndTeamId(1L, 2L)).thenReturn(List.of(objective3));
        when(objectiveRepository.findByQuarterIdAndTeamId(1L, 3L)).thenReturn(Collections.emptyList());
        when(overviewMapper.toDto(team1, List.of(objective1))).thenReturn(overview1WithoutObjective2);
        when(overviewMapper.toDto(team2, List.of(objective3))).thenReturn(overview2);
        when(overviewMapper.toDto(team3, Collections.emptyList())).thenReturn(overview3);

        List<OverviewDto> overview = overviewService.getOverview(Collections.emptyList(), 1L);

        assertEquals(3, overview.size());
        assertEquals(1, overview.get(0).getTeam().getId());
        assertEquals(1, overview.get(0).getObjectives().size());
        assertEquals(3, overview.get(1).getTeam().getId());
        assertEquals(0, overview.get(1).getObjectives().size());
        assertEquals(2, overview.get(2).getTeam().getId());
        assertEquals(1, overview.get(2).getObjectives().size());
    }

}
