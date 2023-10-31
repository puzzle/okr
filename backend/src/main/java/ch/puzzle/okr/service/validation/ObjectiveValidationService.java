package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.ObjectiveRepository;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
public class ObjectiveValidationService
        extends ValidationBase<Objective, Long, ObjectiveRepository, ObjectivePersistenceService> {

    public ObjectiveValidationService(ObjectivePersistenceService objectivePersistenceService) {
        super(objectivePersistenceService);
    }

    @Override
    public void validateOnCreate(Objective model) {
        throwExceptionWhenModelIsNull(model);
        throwExceptionWhenIdIsNotNull(model.getId());
        throwExceptionWhenModifiedByIsSet(model);
        validate(model);
    }

    @Override
    public void validateOnUpdate(Long id, Objective model) {
        throwExceptionWhenModelIsNull(model);
        throwExceptionWhenIdIsNull(model.getId());
        throwExceptionWhenIdHasChanged(id, model.getId());
        throwExceptionWhenModifiedByIsNull(model);
        Objective savedObjective = doesEntityExist(id);
        throwExceptionWhenTeamHasChanged(model.getTeam(), savedObjective.getTeam());
        validate(model);
    }

    private static void throwExceptionWhenModifiedByIsSet(Objective model) {
        if (model.getModifiedBy() != null) {
            throw new ResponseStatusException(BAD_REQUEST,
                    format("Not allowed to set ModifiedBy %s on create", model.getModifiedBy()));
        }
    }

    private static void throwExceptionWhenModifiedByIsNull(Objective model) {
        if (model.getModifiedBy() == null) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR,
                    format("Something went wrong. ModifiedBy %s is not set.", model.getModifiedBy()));
        }
    }

    private static void throwExceptionWhenTeamHasChanged(Team team, Team savedTeam) {
        if (!Objects.equals(team, savedTeam)) {
            throw new ResponseStatusException(BAD_REQUEST, format(
                    "The team can not be changed (new team %s, old team %s)", team.getName(), savedTeam.getName()));
        }
    }
}
