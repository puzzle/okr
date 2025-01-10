package ch.puzzle.okr.exception;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.dto.ErrorDto;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OkrResponseStatusException extends ResponseStatusException {

    private final List<ErrorDto> errors;

    public static OkrResponseStatusException of(String messageKey) {
        return new OkrResponseStatusException(UNAUTHORIZED, messageKey, List.of());
    }

    public static OkrResponseStatusException of(ErrorKey errorKey, String param) {
        return new OkrResponseStatusException(UNAUTHORIZED, errorKey, param);
    }

    public OkrResponseStatusException(HttpStatus status, String messageKey) {
        this(status, ErrorDto.of(messageKey, List.of()));
    }

    public OkrResponseStatusException(HttpStatus status, ErrorKey errorKey) {
        this(status, ErrorDto.of(errorKey, List.of()));
    }

    public OkrResponseStatusException(HttpStatus status, ErrorKey errorKey, List<Object> objectParams) {
        this(status, ErrorDto.of(errorKey, objectParams));
    }

    public OkrResponseStatusException(HttpStatus status, String messageKey, List<Object> objectParams) {
        this(status, ErrorDto.of(messageKey, objectParams));
    }

    public OkrResponseStatusException(HttpStatus status, String messageKey, String param) {
        this(status, ErrorDto.of(messageKey, List.of(param)));
    }

    public OkrResponseStatusException(HttpStatus status, ErrorKey errorKey, String param) {
        this(status, ErrorDto.of(errorKey, param));
    }

    public OkrResponseStatusException(HttpStatus status, ErrorDto error) {
        super(status, error.errorKey());
        this.errors = List.of(error);
    }

    public OkrResponseStatusException(HttpStatus status, List<ErrorDto> errors) {
        super(status, errors.get(0).errorKey());
        this.errors = errors;
    }

    public List<ErrorDto> getErrors() {
        return errors;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OkrResponseStatusException that)) {
            return false;
        }
        return Objects.equals(getErrors(), that.getErrors());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getErrors());
    }
}
