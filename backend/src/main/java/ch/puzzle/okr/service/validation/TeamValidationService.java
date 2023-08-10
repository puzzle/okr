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
    public void validateOnCreate(Team model) {
        isModelNull(model);
        throwExceptionWhenIdIsNotNull(model.getId());

        validate(model);
    }

    @Override
    public void validateOnUpdate(Long id, Team model) {
        isModelNull(model);
        throwExceptionWhenIdIsNull(model.getId());

        doesEntityExist(id, modelName());
        validate(model);
    }
}
