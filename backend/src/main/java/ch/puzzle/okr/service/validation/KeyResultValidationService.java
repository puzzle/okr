package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.Constants;
import ch.puzzle.okr.models.ErrorMsg;
import ch.puzzle.okr.models.OkrResponseStatusException;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.service.persistence.KeyResultPersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class KeyResultValidationService
        extends ValidationBase<KeyResult, Long, KeyResultRepository, KeyResultPersistenceService> {

    public KeyResultValidationService(KeyResultPersistenceService keyResultPersistenceService) {
        super(keyResultPersistenceService);
    }

    @Override
    public void validateOnCreate(KeyResult model) {
        throwExceptionWhenModelIsNull(model);
        throwExceptionWhenIdIsNotNull(model.getId());
        validate(model);
    }

    @Override
    public void validateOnUpdate(Long id, KeyResult model) {
        throwExceptionWhenModelIsNull(model);
        throwExceptionWhenIdIsNull(model.getId());
        throwExceptionWhenIdHasChanged(id, model.getId());
        KeyResult savedKeyResult = doesEntityExist(id);
        throwExceptionWhenObjectiveHasChanged(model, savedKeyResult);
        validate(model);
    }

    private static void throwExceptionWhenObjectiveHasChanged(KeyResult keyResult, KeyResult savedKeyResult) {
        if (!Objects.equals(keyResult.getObjective().getId(), savedKeyResult.getObjective().getId())) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMsg.ATTRIBUTE_CANNOT_CHANGE,
                    List.of(Constants.OBJECTIVE, Constants.KEY_RESULT));
        }
    }
}
