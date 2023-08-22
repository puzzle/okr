package ch.puzzle.okr.service;

import ch.puzzle.okr.Constants;
import ch.puzzle.okr.dto.ObjectiveDto;
import ch.puzzle.okr.dto.OverviewDto;
import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.mapper.OverviewMapper;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.business.ObjectiveBusinessService;
import ch.puzzle.okr.service.business.TeamBusinessService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OverviewServiceTest {
    @MockBean
    OverviewMapper overviewMapper = Mockito.mock(OverviewMapper.class);
    @MockBean
    ObjectiveBusinessService objectiveBusinessService = Mockito.mock(ObjectiveBusinessService.class);
    @MockBean
    TeamBusinessService teamBusinessService = Mockito.mock(TeamBusinessService.class);

    OverviewDto overviewTeam1Objective1And2;
    OverviewDto overviewTeam2Objective3;
    OverviewDto overviewTeam3;
    Objective objective1;
    Objective objective2;
    Objective objective3;

    Team team1;
    Team team2;
    Team team3;

    List<Team> teamList;

    @InjectMocks
    OverviewService overviewService;

    @BeforeEach
    void setUp() {
        this.team1 = Team.Builder.builder().withId(1L).withName(Constants.TEAM_PUZZLE).build();
        this.team2 = Team.Builder.builder().withId(2L).withName("B.Team 2").build();
        this.team3 = Team.Builder.builder().withId(3L).withName("A.Team 3").build();
        this.objective1 = Objective.Builder.builder().withId(1L).withTitle("Objective 1")
                .withQuarter(Quarter.Builder.builder().withId(1L).build()).withTeam(team1).build();
        this.objective2 = Objective.Builder.builder().withId(2L).withTitle("Objective 2")
                .withQuarter(Quarter.Builder.builder().withId(2L).build()).withTeam(team1).build();
        this.objective3 = Objective.Builder.builder().withId(3L).withTitle("Objective 3")
                .withQuarter(Quarter.Builder.builder().withId(1L).build()).withTeam(team2).build();

        this.overviewTeam1Objective1And2 = new OverviewDto(new TeamDto(team1.getId(), team1.getName()),
                List.of(new ObjectiveDto(objective1.getId(), objective1.getTitle(), null, null, null, null, null,
                        objective1.getQuarter().getId(), null, null, null),
                        new ObjectiveDto(objective2.getId(), objective2.getTitle(), null, null, null, null, null,
                                objective2.getQuarter().getId(), null, null, null)));

        this.overviewTeam2Objective3 = new OverviewDto(new TeamDto(team2.getId(), team2.getName()),
                List.of(new ObjectiveDto(objective3.getId(), objective3.getTitle(), null, null, null, null, null,
                        objective3.getQuarter().getId(), null, null, null)));
        this.overviewTeam3 = new OverviewDto(new TeamDto(team3.getId(), team3.getName()), Collections.emptyList());
        this.teamList = List.of(team1, team2, team3);
    }

    @Test
    void shouldGetOverviewWithNoFilters() {
        when(teamBusinessService.getAllTeams(any())).thenReturn(teamList);
        when(objectiveBusinessService.getObjectiveByTeamIdAndQuarterId(1L, null))
                .thenReturn(List.of(objective1, objective2));
        when(objectiveBusinessService.getObjectiveByTeamIdAndQuarterId(2L, null)).thenReturn(List.of(objective3));
        when(objectiveBusinessService.getObjectiveByTeamIdAndQuarterId(3L, null)).thenReturn(Collections.emptyList());

        when(overviewMapper.toDto(team1, List.of(objective1, objective2))).thenReturn(overviewTeam1Objective1And2);
        when(overviewMapper.toDto(team2, List.of(objective3))).thenReturn(overviewTeam2Objective3);
        when(overviewMapper.toDto(team3, Collections.emptyList())).thenReturn(overviewTeam3);

        List<OverviewDto> overview = overviewService.getOverview(Collections.emptyList(), null);

        assertEquals(3, overview.size());
        assertEquals(1, overview.get(0).team().id());
        assertEquals(2, overview.get(0).objectives().size());
        assertEquals(2, overview.get(1).team().id());
        assertEquals(0, overview.get(2).objectives().size());
    }

    @Test
    void shouldGetOverviewWithTeamsFilter() {
        when(teamBusinessService.getAllTeams(List.of(1L, 3L))).thenReturn(List.of(team1, team3));
        when(objectiveBusinessService.getObjectiveByTeamIdAndQuarterId(1L, null))
                .thenReturn(List.of(objective1, objective2));
        when(objectiveBusinessService.getObjectiveByTeamIdAndQuarterId(3L, null)).thenReturn(Collections.emptyList());
        when(overviewMapper.toDto(team1, List.of(objective1, objective2))).thenReturn(overviewTeam1Objective1And2);
        when(overviewMapper.toDto(team3, Collections.emptyList())).thenReturn(overviewTeam3);

        List<OverviewDto> overview = overviewService.getOverview(List.of(1L, 3L), null);

        assertEquals(2, overview.size());
        assertEquals(1, overview.get(0).team().id());
        assertEquals(2, overview.get(0).objectives().size());
        assertEquals(0, overview.get(1).objectives().size());
    }

    @Test
    void shouldGetOverviewWithQuarterFilter() {
        when(teamBusinessService.getAllTeams(any())).thenReturn(teamList);
        when(overviewMapper.toDto(eq(team1), anyList())).thenReturn(overviewTeam1Objective1And2);
        when(overviewMapper.toDto(eq(team2), anyList())).thenReturn(overviewTeam2Objective3);
        when(overviewMapper.toDto(team3, Collections.emptyList())).thenReturn(overviewTeam3);

        List<OverviewDto> overview = overviewService.getOverview(Collections.emptyList(), 1L);

        assertEquals(3, overview.size());
        assertEquals(1, overview.get(0).team().id());
        assertEquals(2, overview.get(0).objectives().size());
        assertEquals(2, overview.get(1).team().id());
        assertEquals(1, overview.get(1).objectives().size());
        assertEquals(0, overview.get(2).objectives().size());
    }

    @Test
    void shouldGetOverViewOfQuarterFilterAndTeamFilter() {
        OverviewDto overviewTeam1Objective1 = new OverviewDto(new TeamDto(team1.getId(), team1.getName()),
                List.of(new ObjectiveDto(objective1.getId(), objective1.getTitle(), null, null, null, null, null,
                        objective1.getQuarter().getId(), null, null, null)));
        OverviewDto overviewTeam3Objective3 = new OverviewDto(new TeamDto(team3.getId(), team3.getName()),
                List.of(new ObjectiveDto(objective1.getId(), objective1.getTitle(), null, null, null, null, null,
                        objective3.getQuarter().getId(), null, null, null)));

        when(teamBusinessService.getAllTeams(List.of(1L, 3L))).thenReturn(List.of(team1, team3));
        when(overviewMapper.toDto(eq(team1), anyList())).thenReturn(overviewTeam1Objective1);
        when(overviewMapper.toDto(eq(team3), anyList())).thenReturn(overviewTeam3Objective3);

        List<OverviewDto> overview = overviewService.getOverview(List.of(1L, 3L), 1L);

        assertEquals(2, overview.size());
        assertEquals(1, overview.get(0).team().id());
        assertEquals(1, overview.get(0).objectives().size());
        assertEquals(3, overview.get(1).team().id());
        assertEquals(1, overview.get(1).objectives().size());
    }

}
