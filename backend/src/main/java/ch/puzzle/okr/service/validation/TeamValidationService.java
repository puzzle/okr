package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.ErrorMsg;
import ch.puzzle.okr.models.OkrResponseStatusException;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.TeamRepository;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TeamValidationService extends ValidationBase<Team, Long, TeamRepository, TeamPersistenceService> {

    public TeamValidationService(TeamPersistenceService teamPersistenceService) {
        super(teamPersistenceService);
    }

    @Override
    public void validateOnCreate(Team model) {
        throwExceptionWhenModelIsNull(model);
        throwExceptionWhenIdIsNotNull(model.getId());
        checkIfTeamWithNameAlreadyExists(model.getName(), model.getId());
        validate(model);
    }

    @Override
    public void validateOnUpdate(Long id, Team model) {
        throwExceptionWhenModelIsNull(model);
        throwExceptionWhenIdIsNull(id);
        throwExceptionWhenIdHasChanged(id, model.getId());
        doesEntityExist(model.getId());
        checkIfTeamWithNameAlreadyExists(model.getName(), model.getId());
        validate(model);
    }

    private void checkIfTeamWithNameAlreadyExists(String name, Long id) {
        List<Team> filteredTeam = this.getPersistenceService().findTeamsByName(name).stream()
                .filter(team -> !Objects.equals(team.getId(), id)).toList();
        if (!filteredTeam.isEmpty()) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMsg.ALREADY_EXISTS_SAME_NAME,
                    List.of("Team", name));
        }
    }
}
