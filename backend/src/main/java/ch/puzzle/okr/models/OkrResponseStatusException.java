package ch.puzzle.okr.models;

import ch.puzzle.okr.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class OkrResponseStatusException extends ResponseStatusException {

    private final List<ErrorDto> errors;

    public OkrResponseStatusException(HttpStatus status, String errorKey) {
        this(status, errorKey, List.of());
    }

    public OkrResponseStatusException(HttpStatus status, String errorKey, List<Object> objectParams) {
        this(status, List.of(new ErrorDto(errorKey, objectParams)));
    }

    public OkrResponseStatusException(HttpStatus status, String errorKey, String param) {
        this(status, errorKey, List.of(param));
    }

    public OkrResponseStatusException(HttpStatus status, ErrorDto error) {
        this(status, List.of(error));
    }

    public OkrResponseStatusException(HttpStatus status, List<ErrorDto> errors) {
        super(status, errors.get(0).getErrorKey());
        this.errors = errors;
    }

    public List<ErrorDto> getErrors() {
        return errors;
    }
}
