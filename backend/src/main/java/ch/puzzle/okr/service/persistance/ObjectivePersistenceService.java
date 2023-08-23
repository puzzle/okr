package ch.puzzle.okr.service.persistance;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.ObjectiveRepository;
import org.springframework.stereotype.Service;

@Service
public class ObjectivePersistenceService extends PersistenceBase<Objective, Long> {
    protected ObjectivePersistenceService(ObjectiveRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return "Objective";
    }

    public Integer countByTeamAndQuarter(Team team, Quarter quarter) {
        return ((ObjectiveRepository) this.repository).countByTeamAndQuarter(team, quarter);
    }
}
