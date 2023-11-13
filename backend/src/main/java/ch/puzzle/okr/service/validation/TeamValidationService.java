package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.TeamRepository;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TeamValidationService extends ValidationBase<Team, Long, TeamRepository, TeamPersistenceService> {

    public TeamValidationService(TeamPersistenceService teamPersistenceService) {
        super(teamPersistenceService);
    }

    public void validateOnGetActiveObjectives(Team team) {
        throwExceptionWhenModelIsNull(team);
        throwExceptionWhenIdIsNull(team.getId());
        doesEntityExist(team.getId());
    }

    @Override
    public void validateOnCreate(Team model) {
        throwExceptionWhenModelIsNull(model);
        throwExceptionWhenIdIsNotNull(model.getId());
        checkIfTeamWithNameAlreadyExists(model.getName(), "can't create team with already existing name");
        validate(model);
    }

    @Override
    public void validateOnUpdate(Long id, Team model) {
        throwExceptionWhenModelIsNull(model);
        throwExceptionWhenIdIsNull(id);
        throwExceptionWhenIdHasChanged(id, model.getId());
        doesEntityExist(model.getId());
        checkIfTeamWithNameAlreadyExists(model.getName(), "can't update to team with already existing name");
        validate(model);
    }

    private void checkIfTeamWithNameAlreadyExists(String name, String message) {
        if (!this.getPersistenceService().findTeamsByName(name).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }
}
