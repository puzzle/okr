package ch.puzzle.okr.service.persistence;

import static ch.puzzle.okr.Constants.TEAM;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.TeamRepository;
import java.util.List;

import ch.puzzle.okr.service.persistence.customCrud.SoftDelete;
import org.springframework.stereotype.Service;

@Service
public class TeamPersistenceService extends PersistenceBase<Team, Long, TeamRepository> {

    protected TeamPersistenceService(TeamRepository repository) {
        super(repository, new SoftDelete<>());
    }

    @Override
    public String getModelName() {
        return TEAM;
    }

    public List<Team> findTeamsByName(String name) {
        return getRepository().findTeamsByNameAndIsDeletedFalse(name);
    }
}
