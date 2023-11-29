package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Long> findTeamIdsByOrganisationName(String organisationName) {
        return findTeamIdsByOrganisationNames(List.of(organisationName));
    }

    public List<Long> findTeamIdsByOrganisationNames(List<String> organisationNames) {
        return getRepository().findTeamIdsByOrganisationNames(organisationNames);
    }

    public List<Team> findTeamsByName(String name) {
        return getRepository().findTeamsByName(name);
    }
}
