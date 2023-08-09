package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Team;
import org.springframework.stereotype.Service;

@Service
public class TeamValidationService extends ValidationBase<Team> {

    @Override
    public void validateOnSave(Team model) {
        isModelNull(model);
        isIdNull(model.getId());

        validate(model);
    }

    @Override
    public void validateOnUpdate(Team model) {
        isModelNull(model);
        isIdNull(model.getId());

        validate(model);
    }
}
