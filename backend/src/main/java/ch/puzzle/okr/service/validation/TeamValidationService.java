package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.TeamRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TeamValidationService extends ValidationBase<Long, Team> {

    private final TeamRepository teamRepository;

    public TeamValidationService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public void validateOnGet(Long id) {
        isIdNull(id);
        doesEntityExist(id, modelName());
    }

    @Override
    public void validateOnCreate(Team model) {
        isModelNull(model, modelName());
        validate(model);
    }

    @Override
    public void validateOnUpdate(Team model) {
        isModelNull(model, modelName());
        isIdNull(model.getId());

        validate(model);
    }

    @Override
    public void validateOnDelete(Long id) {
        isIdNull(id);
        doesEntityExist(id, modelName());
    }

    @Override
    protected String modelName() {
        return "Team";
    }

    @Override
    public void doesEntityExist(Long id, String modelName) {
        teamRepository.findById(id).ifPresent(team -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("%s with id %d not found", modelName(), team.getId()));
        });
    }
}
