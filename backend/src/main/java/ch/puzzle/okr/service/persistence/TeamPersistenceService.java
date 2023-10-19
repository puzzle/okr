package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class TeamPersistenceService extends PersistenceBase<Team, Long, TeamRepository> {

    protected TeamPersistenceService(TeamRepository repository) {
        super(repository);
    }

    public Team findByRoleName(String roleName) {
        return getRepository().findByRoleName(roleName).orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED,
                String.format("role name %s does not match team", roleName)));
    }

    public List<Long> findByRoleNames(List<String> roleNames) {
        return getRepository().findByRoleNames(roleNames);
    }

    @Override
    public String getModelName() {
        return "Team";
    }
}
