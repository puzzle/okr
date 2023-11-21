package ch.puzzle.okr.models;

import org.springframework.data.annotation.Transient;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class OkrResponseStatusException extends ResponseStatusException {

    public final List<ErrorDto> errors;

    public OkrResponseStatusException(HttpStatus status, String errorKey) {
        this(status, errorKey, List.of());
    }

    public OkrResponseStatusException(HttpStatus status, String errorKey, List<Object> objectParams) {
        this(status, List.of(new ErrorDto(errorKey, objectParams)));
    }

    public OkrResponseStatusException(HttpStatus status, List<ErrorDto> errors) {
        super(status);
        this.errors = errors;
    }
}
