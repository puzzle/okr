package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.MessageKey;
import ch.puzzle.okr.service.persistence.PersistenceBase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
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
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.MODEL_NULL,
                    persistenceService.getModelName());
        }
    }

    public void throwExceptionWhenIdIsNull(ID id) {
        if (id == null) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.ATTRIBUTE_NULL,
                    List.of("ID", persistenceService.getModelName()));
        }
    }

    protected void throwExceptionWhenIdIsNotNull(ID id) {
        if (id != null) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, MessageKey.ATTRIBUTE_NOT_NULL,
                    List.of("ID", persistenceService.getModelName()));
        }
    }

    protected void throwExceptionWhenIdHasChanged(ID id, ID modelId) {
        if (!Objects.equals(id, modelId)) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.ATTRIBUTE_CHANGED,
                    List.of("ID", id, modelId));
        }
    }

    public void validate(T model) {
        Set<ConstraintViolation<T>> violations = validator.validate(model);
        processViolations(violations);
    }

    private void processViolations(Set<ConstraintViolation<T>> violations) {
        if (!violations.isEmpty()) {
            List<ErrorDto> list = violations.stream().map(e -> {
                List<Object> attributes = new ArrayList<>(
                        List.of(e.getPropertyPath().toString(), persistenceService.getModelName()));
                attributes.addAll(getAttributes(e.getMessage(), e.getMessageTemplate()));
                String errorKey = e.getMessageTemplate().replaceAll("_\\{.*", "");
                return ErrorDto.of(errorKey, attributes);
            }).toList();
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, list);
        }
    }

    private List<String> getAttributes(String message, String messageTemplate) {
        String patternString = messageTemplate.replaceAll("\\{([^}]*)\\}", "(.*)");
        Pattern p = Pattern.compile(patternString);
        Matcher m = p.matcher(message);
        List<String> arr = new ArrayList<>();
        m.find();
        for (int i = 1; i < m.groupCount() + 1; i++) {
            arr.add(m.group(i));
        }
        return arr;
    }
}
