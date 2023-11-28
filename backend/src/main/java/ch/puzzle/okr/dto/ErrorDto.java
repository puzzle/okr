package ch.puzzle.okr.dto;

import ch.puzzle.okr.ErrorKey;

import java.util.List;

public record ErrorDto(String errorKey, List<String> params) {

    public static ErrorDto of(ErrorKey errorKey, String param) {
        return of(errorKey.name(), List.of(param));
    }

    public static ErrorDto of(ErrorKey errorKey, List<Object> params) {
        return of(errorKey.name(), params);
    }

    public static ErrorDto of(String messageKey, List<Object> params) {
        return new ErrorDto(messageKey, params.stream().map(Object::toString).toList());
    }
}
