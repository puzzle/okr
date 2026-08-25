package ch.puzzle.okr.service.validation;

import static ch.puzzle.okr.Constants.TEAM;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.team.Team;
import ch.puzzle.okr.models.team.TeamStatus;
import ch.puzzle.okr.repository.TeamRepository;
import ch.puzzle.okr.service.persistence.QuarterPersistenceService;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TeamValidationService extends ValidationBase<Team, Long, TeamRepository, TeamPersistenceService> {

    private final QuarterPersistenceService quarterPersistenceService;

    public TeamValidationService(TeamPersistenceService teamPersistenceService,
                                 QuarterPersistenceService quarterPersistenceService) {
        super(teamPersistenceService);
        this.quarterPersistenceService = quarterPersistenceService;
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
        throwExceptionIfTeamIsArchived(id);
        validate(model);
    }

    public void validateOnArchive(Team model, LocalDate markedAsArchivedAt, LocalDate firstQuarterStartDate,
                                  LocalDate lastQuarterEndDate) {
        throwExceptionWhenModelIsNull(model);
        validateDate(markedAsArchivedAt, firstQuarterStartDate, lastQuarterEndDate);
        validateTeamStatusToNotEqual(model, TeamStatus.ARCHIVED, ErrorKey.TEAM_IS_ALREADY_ARCHIVED);
        validate(model);
    }

    public void validateOnUnarchive(Team model) {
        validateTeamStatusToNotEqual(model, TeamStatus.ACTIVE, ErrorKey.TEAM_IS_ALREADY_ACTIVE);
        validate(model);
    }

    private void validateDate(LocalDate date, LocalDate startDate, LocalDate endDate) {

        if (date == null || date.isBefore(startDate) || date.isAfter(endDate)) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST,
                                                 ErrorKey.DATE_NOT_VALID,
                                                 List.of("markedAsArchivedAt", TEAM, startDate, endDate));
        }
    }

    private void validateTeamStatusToNotEqual(Team model, TeamStatus statusToCheck, ErrorKey errorKey) {
        if (model.getStatus() == statusToCheck) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, errorKey, List.of(model.getName()));
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

    public void throwExceptionIfTeamIsArchived(Long id) {
        Team existingTeam = this.getPersistenceService().findById(id);

        if (existingTeam.getStatus() == TeamStatus.ARCHIVED) {
            throw new OkrResponseStatusException(HttpStatus.FORBIDDEN,
                                                 ErrorKey.TEAM_IS_ARCHIVED,
                                                 List.of(existingTeam.getName()));
        }
    }
}
