package ch.puzzle.okr.security.helper;

public class UrlHelper {

    public static String extractTenantFromIssUrl(String issUrl) {
        String[] issUrlParts = issUrl.split("/");
        return issUrlParts[issUrlParts.length - 1];
    }
}
