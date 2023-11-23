package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.ErrorMsg;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.OkrResponseStatusException;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.ObjectiveRepository;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

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

            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMsg.ATTRIBUTE_SET_FORBIDDEN,
                    List.of("ModifiedBy", model.getModifiedBy()));
        }
    }

    private static void throwExceptionWhenModifiedByIsNull(Objective model) {
        if (model.getModifiedBy() == null) {
            throw new OkrResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMsg.ATTRIBUTE_NOT_SET,
                    List.of(model.getModifiedBy()));
        }
    }

    private static void throwExceptionWhenTeamHasChanged(Team team, Team savedTeam) {
        if (!Objects.equals(team, savedTeam)) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMsg.ATTRIBUTE_CANNOT_CHANGE,
                    List.of("Team", "Objective"));
        }
    }
}
