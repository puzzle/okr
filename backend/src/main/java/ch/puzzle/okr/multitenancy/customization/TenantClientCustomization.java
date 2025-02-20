package ch.puzzle.okr.multitenancy.customization;

import java.util.Map;

public record TenantClientCustomization(String favicon, String logo, String triangles, String backgroundLogo,
        String title, String helpSiteUrl, Map<String, String> customStyles) {
}
