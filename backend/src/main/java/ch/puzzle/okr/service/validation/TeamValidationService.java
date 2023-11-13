package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.TeamRepository;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import org.springframework.stereotype.Service;

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
        validate(model);
    }

    @Override
    public void validateOnUpdate(Long id, Team model) {
        throwExceptionWhenModelIsNull(model);
        throwExceptionWhenIdIsNull(id);
        throwExceptionWhenIdHasChanged(id, model.getId());
        doesEntityExist(model.getId());
        validate(model);
    }
}
