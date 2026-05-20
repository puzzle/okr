package ch.puzzle.okr.service.validation;

import static ch.puzzle.okr.Constants.TEAM;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.team.Team;
import ch.puzzle.okr.models.team.TeamStatus;
import ch.puzzle.okr.repository.TeamRepository;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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

    public void validateOnArchive(Team model, LocalDateTime markedAsArchivedAt) {
        throwExceptionWhenModelIsNull(model);
        validateValidDate(markedAsArchivedAt);
        validateThatModelIsNotAlreadyArchived(model);
        validate(model);
    }

    private void validateValidDate(LocalDateTime date) {
        if (date == null) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST,
                                                 ErrorKey.ATTRIBUTE_NULL,
                                                 List.of("markedAsArchivedAt", TEAM));
        }
    }

    public void validateThatModelIsNotAlreadyArchived(Team model) {
        if (model.getStatus() == TeamStatus.ARCHIVED) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST,
                                                 ErrorKey.TEAM_IS_ALREADY_ARCHIVED,
                                                 List.of(model.getName()));
        }
    }

    private void checkIfTeamWithNameAlreadyExists(String name, Long id) {
        List<Team> filteredTeam = this
                .getPersistenceService()
                .findTeamsByName(name)
                .stream()
                .filter(team -> !Objects.equals(team.getId(), id))
                .toList();
        if (!filteredTeam.isEmpty()) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST,
                                                 ErrorKey.ALREADY_EXISTS_SAME_NAME,
                                                 List.of(TEAM, name));
        }
    }
}
