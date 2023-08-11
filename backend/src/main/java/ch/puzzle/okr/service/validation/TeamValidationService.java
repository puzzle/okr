package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.TeamRepository;
import org.springframework.stereotype.Service;

@Service
public class TeamValidationService extends ValidationBase<Team, Long> {

    public TeamValidationService(TeamRepository teamRepository) {
        super(teamRepository);
    }

    @Override
    public void validateOnGet(Long id) {
        isIdNull(id);
        doesEntityExist(id);
    }

    @Override
    public void validateOnCreate(Team model) {
        isModelNull(model);
        validate(model);
    }

    @Override
    public void validateOnUpdate(Long id, Team model) {
        isModelNull(model);
        isIdNull(model.getId());

        doesEntityExist(id);
        validate(model);
    }

    @Override
    public void validateOnDelete(Long id) {
        isIdNull(id);
        doesEntityExist(id);
    }

    @Override
    protected String modelName() {
        return "Team";
    }
}
