package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.persistance.TeamPersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TeamValidationService extends ValidationBase<Team, Long> {

    public TeamValidationService(TeamPersistenceService teamPersistenceService) {
        super(teamPersistenceService);
    }

    @Override
    public void validateOnGet(Long id) {
        isIdNull(id);
    }

    @Override
    public void validateOnCreate(Team model) {
        isModelNull(model);
        if (model.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Model %s cannot have id while create. Found id %d",
                            persistenceService.getModelName(), model.getId()));
        }
        validate(model);
    }

    @Override
    public void validateOnUpdate(Long id, Team model) {
        isModelNull(model);
        isIdNull(model.getId());

        validate(model);
    }

    @Override
    public void validateOnDelete(Long id) {
        isIdNull(id);
        doesEntityExist(id);
    }
}
