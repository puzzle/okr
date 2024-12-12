package ch.puzzle.okr.security.helper;

import java.util.Optional;

public class UrlHelper {

    private UrlHelper() {
    }

    public static Optional<String> extractTenantFromIssUrl(String issUrl) {
        if (issUrl == null)
            return Optional.empty();
        String[] issUrlParts = issUrl.split("/");
        String tenant = issUrlParts[issUrlParts.length - 1];
        return Optional.of(tenant);
    }
}
