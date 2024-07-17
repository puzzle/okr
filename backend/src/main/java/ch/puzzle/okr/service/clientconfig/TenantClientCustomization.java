package ch.puzzle.okr.service.clientconfig;

import java.util.Map;

public record TenantClientCustomization(String favicon, String logo, String triangles, String backgroundLogo,
        String title, Map<String, String> customStyles) {
}
