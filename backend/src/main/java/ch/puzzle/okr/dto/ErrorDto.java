package ch.puzzle.okr.dto;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ErrorDto errorDto = (ErrorDto) o;
        return Objects.equals(errorKey, errorDto.errorKey) && Objects.equals(params, errorDto.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorKey, params);
    }
}
