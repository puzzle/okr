package ch.puzzle.okr.dto;

import java.util.List;

public class ErrorDto {
    private final String errorKey;
    private final List<String> params;

    public ErrorDto(String errorKey, List<Object> params) {
        this.errorKey = errorKey;
        this.params = params.stream().map(Object::toString).toList();
    }

    public ErrorDto(String errorKey, String param) {
        this(errorKey, List.of(param));
    }

    public String getErrorKey() {
        return errorKey;
    }

    public List<String> getParams() {
        return params;
    }
}
