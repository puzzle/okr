package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.persistance.TeamPersistenceService;
import org.springframework.stereotype.Service;

@Service
public class TeamValidationService extends ValidationBase<Team, Long> {

    public TeamValidationService(TeamPersistenceService teamPersistenceService) {
        super(teamPersistenceService);
    }

    @Override
    @Deprecated
    public void validateOnCreate(Team model) {
        throwExceptionIfModelIsNull(model);
        throwExceptionWhenIdIsNotNull(model.getId());

        validate(model);
    }

    @Override
    @Deprecated
    public void validateOnUpdate(Long id, Team model) {
        throwExceptionIfModelIsNull(model);
        throwExceptionWhenIdIsNull(model.getId());

        doesEntityExist(id);
        validate(model);
    }

    public void validateOnGetActiveObjectives(Team team) {
        throwExceptionIfModelIsNull(team);
        throwExceptionWhenIdIsNull(team.getId());
        doesEntityExist(team.getId());
    }
}
