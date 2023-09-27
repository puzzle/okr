package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import org.springframework.stereotype.Service;

@Service
public class TeamValidationService extends ValidationBase<Team, Long> {

    public TeamValidationService(TeamPersistenceService teamPersistenceService) {
        super(teamPersistenceService);
    }

    public void validateOnGetActiveObjectives(Team team) {
        throwExceptionIfModelIsNull(team);
        throwExceptionWhenIdIsNull(team.getId());
        doesEntityExist(team.getId());
    }

    @Override
    public void validateOnCreate(Team model) {
        throw new IllegalCallerException("This method must not be called");
    }

    @Override
    public void validateOnUpdate(Long id, Team model) {
        throw new IllegalCallerException("This method must not be called because there is no update of quarters");
    }
}
