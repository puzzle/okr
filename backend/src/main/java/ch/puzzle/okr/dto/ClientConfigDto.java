package ch.puzzle.okr.dto;

import java.util.HashMap;

public record ClientConfigDto(
        String activeProfile,
        String issuer,
        String clientId,
        String favicon,
        String logo,
        String title,
        HashMap<String, String> customStyles
) {
}
