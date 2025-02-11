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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;

/**
 * @param <T>
 *            the Type or entity of the repository
 * @param <I>
 *            the Identifier or primary key of the entity
 * @param <R>
 *            the Repository of the entity
 * @param <P>
 *            the Persistence Service of this repository and entity
 */
public abstract class ValidationBase<T, I, R extends CrudRepository<T, I>, P extends PersistenceBase<T, I, R>> {
    private final Validator validator;
    private final P persistenceService;

    ValidationBase(P persistenceService) {
        this.persistenceService = persistenceService;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    public P getPersistenceService() {
        return persistenceService;
    }

    public void validateOnGet(I i) {
        throwExceptionWhenIdIsNull(i);
    }

    public abstract void validateOnCreate(T model);

    public abstract void validateOnUpdate(I i, T model);

    public T validateOnDelete(I i) {
        throwExceptionWhenIdIsNull(i);
        return doesEntityExist(i);
    }

    public T doesEntityExist(I i) {
        return persistenceService.findById(i);
    }

    public void throwExceptionWhenModelIsNull(T model) {
        if (model == null) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST,
                                                 ErrorKey.MODEL_NULL,
                                                 persistenceService.getModelName());
        }
    }

    public void throwExceptionWhenIdIsNull(I i) {
        if (i == null) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST,
                                                 ErrorKey.ATTRIBUTE_NULL,
                                                 List.of("ID", persistenceService.getModelName()));
        }
    }

    protected void throwExceptionWhenIdIsNotNull(I i) {
        if (i != null) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST,
                                                 MessageKey.ATTRIBUTE_NOT_NULL,
                                                 List.of("ID", persistenceService.getModelName()));
        }
    }

    protected void throwExceptionWhenIdHasChanged(I i, I modelI) {
        if (!Objects.equals(i, modelI)) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST,
                                                 ErrorKey.ATTRIBUTE_CHANGED,
                                                 List.of("ID", i, modelI));
        }
    }

    public void validate(T model) {
        Set<ConstraintViolation<T>> violations = validator.validate(model);
        processViolations(violations);
    }

    private void processViolations(Set<ConstraintViolation<T>> violations) {
        if (!violations.isEmpty()) {
            List<ErrorDto> list = createErrorDtos(violations);
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, list);
        }
    }

    private List<ErrorDto> createErrorDtos(Set<ConstraintViolation<T>> violations) {
        return violations
                .stream() //
                .map(e -> { //
                    String path = e.getPropertyPath().toString(); //
                    List<Object> attributes = new ArrayList<>(List.of(path, persistenceService.getModelName())); //
                    attributes.addAll(getAttributes(e.getMessage(), e.getMessageTemplate())); //
                    String errorKey = e.getMessageTemplate().replaceAll("_\\{.*", ""); //
                    return ErrorDto.of(errorKey, attributes); //
                })
                .toList();
    }

    /**
     * See below for an example
     *
     * @param message
     *            ATTRIBUTE_SIZE_BETWEEN_2_200
     * @param messageTemplate
     *            ATTRIBUTE_SIZE_BETWEEN_{min}_{max}
     *
     * @return [2, 200]
     */
    private List<String> getAttributes(String message, String messageTemplate) {
        String patternString = convertMessageTemplateToRegexStringWithGroups(messageTemplate);
        return extractAttributeValuesFromRegexGroups(message, patternString);
    }

    /**
     * See below for an example
     *
     * @param messageTemplate
     *            ATTRIBUTE_SIZE_BETWEEN_{min}_{max}
     *
     * @return ATTRIBUTE_SIZE_BETWEEN_(.*)_(.*)
     */
    private String convertMessageTemplateToRegexStringWithGroups(String messageTemplate) {
        return messageTemplate.replaceAll("\\{([^}]*)\\}", "(.*)");
    }

    private List<String> extractAttributeValuesFromRegexGroups(String message, String patternString) {
        Pattern p = Pattern.compile(patternString);
        Matcher m = p.matcher(message);
        List<String> attributeValues = new ArrayList<>();
        m.find();
        for (int i = 1; i < m.groupCount() + 1; i++) {
            attributeValues.add(m.group(i));
        }
        return attributeValues;
    }

}
