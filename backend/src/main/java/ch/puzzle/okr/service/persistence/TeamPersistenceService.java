package ch.puzzle.okr.service.persistence;

import java.util.List;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.TeamRepository;

import org.springframework.stereotype.Service;

import static ch.puzzle.okr.Constants.TEAM;

@Service
public class TeamPersistenceService extends PersistenceBase<Team, Long, TeamRepository> {

    protected TeamPersistenceService(TeamRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return TEAM;
    }

    public List<Team> findTeamsByName(String name) {
        return getRepository().findTeamsByName(name);
    }
}
