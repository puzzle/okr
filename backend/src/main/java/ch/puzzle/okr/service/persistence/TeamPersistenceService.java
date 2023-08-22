package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.TeamRepository;
import org.springframework.stereotype.Service;

@Service
public class TeamPersistenceService extends PersistenceBase<Team, Long> {
    protected TeamPersistenceService(TeamRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return "Team";
    }
}
