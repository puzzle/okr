package ch.puzzle.okr.models;

import java.util.List;

public class ErrorDto {
    private final String errorKey;
    private final List<String> params;

    public ErrorDto(String errorKey, List<Object> params) {
        this.errorKey = errorKey;
        this.params = params.stream().map(Object::toString).toList();
    }

    public String getErrorKey() {
        return errorKey;
    }

    public List<String> getParams() {
        return params;
    }
}
