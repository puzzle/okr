package ch.puzzle.okr.models;

import org.springframework.data.annotation.Transient;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class OkrResponseStatusException extends ResponseStatusException {
    @Transient
    public final List<String> params;
    public final ErrorMsgKey errorKey;

    public OkrResponseStatusException(HttpStatus status, ErrorMsgKey errorKey, List<String> params) {
        super(status, errorKey.toString());
        this.params = params;
        this.errorKey = errorKey;
    }

    public OkrResponseStatusException(HttpStatus status, ErrorMsgKey errorKey) {
        super(status, errorKey.toString());
        this.params = List.of();
        this.errorKey = errorKey;
    }
}
