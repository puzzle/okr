package ch.puzzle.okr.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class OkrResponseStatusException extends ResponseStatusException {
    private final List<String> params;
    private final ErrorMsgKey errorKey;

    public OkrResponseStatusException(HttpStatus status, List<String> params, ErrorMsgKey errorKey) {
        super(status);
        this.params = params;
        this.errorKey = errorKey;
    }
}
