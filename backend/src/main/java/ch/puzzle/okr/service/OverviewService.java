package ch.puzzle.okr.service;

import ch.puzzle.okr.dto.OverviewDto;
import ch.puzzle.okr.mapper.OverviewMapper;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.ObjectiveRepository;
import ch.puzzle.okr.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@Service
public class OverviewService {
    private final TeamRepository teamRepository;
    private final OverviewMapper overviewMapper;
    private final ObjectiveRepository objectiveRepository;
    Collator collator = Collator.getInstance(Locale.GERMAN);

    public OverviewService(TeamRepository teamRepository, OverviewMapper overviewMapper,
            ObjectiveRepository objectiveRepository) {
        this.teamRepository = teamRepository;
        this.overviewMapper = overviewMapper;
        this.objectiveRepository = objectiveRepository;
    }

    public List<OverviewDto> getOverview(List<Long> teamFilter, Long quarterFilter) {
        Team puzzleTeam = teamRepository.findByName("Puzzle ITC");
        List<Team> teams;
        if (teamFilter.contains(puzzleTeam.getId()) || teamFilter.isEmpty()) {
            teams = Stream
                    .concat(Stream.of(puzzleTeam),
                            teamRepository.findAllById(teamFilter).stream()
                                    .filter(team -> !"Puzzle ITC".equals(team.getName()))
                                    .sorted((team1, team2) -> collator.compare(team1.getName(), team2.getName())))
                    .toList();
        } else {
            teams = teamRepository.findAllById(teamFilter).stream()
                    .sorted((team1, team2) -> collator.compare(team1.getName(), team2.getName())).toList();
        }
        return teams.stream().map(team -> overviewMapper.toDto(team,
                objectiveRepository.findByQuarterIdAndTeamId(quarterFilter, team.getId()))).toList();
    }
}
