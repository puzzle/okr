package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.models.ErrorMsg;
import ch.puzzle.okr.models.OkrResponseStatusException;
import ch.puzzle.okr.service.persistence.PersistenceBase;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * @param <T>
 *            the Type or entity of the repository
 * @param <ID>
 *            the Identifier or primary key of the entity
 * @param <R>
 *            the Repository of the entity
 * @param <PS>
 *            the Persistence Service of this repository and entity
 */
public abstract class ValidationBase<T, ID, R, PS extends PersistenceBase<T, ID, R>> {
    private final Validator validator;
    private final PS persistenceService;

    ValidationBase(PS persistenceService) {
        this.persistenceService = persistenceService;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    public PS getPersistenceService() {
        return persistenceService;
    }

    public void validateOnGet(ID id) {
        throwExceptionWhenIdIsNull(id);
    }

    public abstract void validateOnCreate(T model);

    public abstract void validateOnUpdate(ID id, T model);

    public T validateOnDelete(ID id) {
        throwExceptionWhenIdIsNull(id);
        return doesEntityExist(id);
    }

    public T doesEntityExist(ID id) {
        return persistenceService.findById(id);
    }

    public void throwExceptionWhenModelIsNull(T model) {
        if (model == null) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMsg.MODEL_NULL,
                    List.of(persistenceService.getModelName()));
            // Given model %s is null
        }
    }

    public void throwExceptionWhenIdIsNull(ID id) {
        if (id == null) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMsg.ATTRIBUTE_NULL,
                    List.of("ID", persistenceService.getModelName()));
            // Id is null
        }
    }

    protected void throwExceptionWhenIdIsNotNull(ID id) {
        if (id != null) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMsg.ATTRIBUTE_NOT_NULL,
                    List.of(persistenceService.getModelName(), id));
            // Model %s cannot have id while create. Found id %s
        }
    }

    protected void throwExceptionWhenIdHasChanged(ID id, ID modelId) {
        if (!Objects.equals(id, modelId)) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMsg.ATTRIBUTE_CHANGED,
                    List.of("ID", id, modelId));

            // Id %s has changed to %s during update
        }
    }

    public void validate(T model) {
        Set<ConstraintViolation<T>> violations = validator.validate(model);
        processViolations(violations);
    }

    private void processViolations(Set<ConstraintViolation<T>> violations) {
        if (!violations.isEmpty()) {
            List<ErrorDto> list = violations.stream().map(e -> {
                List<String> attributes = new ArrayList<>(
                        List.of(e.getPropertyPath().toString(), persistenceService.getModelName()));
                attributes.addAll(getAttributes(e.getMessage(), e.getMessageTemplate()));
                String errorKey = e.getMessage().replaceAll("_\\{.*", "");
                return new ErrorDto(errorKey, Collections.singletonList(attributes));
            }).toList();
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, list);
        }
    }

    private List<String> getAttributes(String message, String messageTemplate) {
        String pattern = messageTemplate.replaceAll("\\{([^}]*)\\}", "(.*)");

        return Pattern.compile(pattern).matcher(message).results().map(MatchResult::group).toList();
    }
}
