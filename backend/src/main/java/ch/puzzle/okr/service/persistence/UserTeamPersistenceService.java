package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.UserTeam;
import ch.puzzle.okr.repository.UserTeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static ch.puzzle.okr.Constants.USER_TEAM;

@Service
public class UserTeamPersistenceService extends PersistenceBase<UserTeam, Long, UserTeamRepository> {
    protected UserTeamPersistenceService(UserTeamRepository repository) {
        super(repository);
    }

    // TODO implement Test
    public void delete(UserTeam userTeam) {
        getRepository().delete(userTeam);
    }

    // TODO implement Test
    public void deleteAll(List<UserTeam> userTeamList) {
        getRepository().deleteAll(userTeamList);
    }

    @Override
    public String getModelName() {
        return USER_TEAM;
    }
}
