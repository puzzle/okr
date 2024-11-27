package ch.puzzle.okr.service.persistence;

import java.util.List;

import ch.puzzle.okr.models.UserTeam;
import ch.puzzle.okr.repository.UserTeamRepository;

import org.springframework.stereotype.Service;

import static ch.puzzle.okr.Constants.USER_TEAM;

@Service
public class UserTeamPersistenceService extends PersistenceBase<UserTeam, Long, UserTeamRepository> {
    protected UserTeamPersistenceService(UserTeamRepository repository) {
        super(repository);
    }

    public void delete(UserTeam userTeam) {
        getRepository().delete(userTeam);
    }

    public void deleteAll(List<UserTeam> userTeamList) {
        getRepository().deleteAll(userTeamList);
    }

    @Override
    public String getModelName() {
        return USER_TEAM;
    }
}
