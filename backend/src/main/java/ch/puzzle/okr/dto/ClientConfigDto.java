package ch.puzzle.okr.dto;

import java.util.Map;

public record ClientConfigDto(String activeProfile, String issuer, String clientId, String favicon, String logo,
        String triangles, String backgroundLogo, String title, String supportSiteURL,
        Map<String, String> customStyles) {
}
