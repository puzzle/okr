package ch.puzzle.okr.dto;

public record UserDto(Long id, int version, String username, String firstname, String lastname, String email,
        boolean isWriteable) {
}
