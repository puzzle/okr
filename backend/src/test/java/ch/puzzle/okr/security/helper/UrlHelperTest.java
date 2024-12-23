package ch.puzzle.okr.security.helper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class UrlHelperTest {

    private static final String PITC = "pitc";

    @ParameterizedTest(name = "extractTenantFromIssUrl() returns tenant if Url contains slash")
    @ValueSource(strings = { "https://sso.puzzle.ch/auth/realms/pitc", "http://localhost:8544/realms/pitc" })
    void extractTenantFromIssUrlReturnsTenantIfUrlContainSlash(String issUrl) {
        // arrange

        // act
        Optional<String> tenantFromIssUrl = UrlHelper.extractTenantFromIssUrl(issUrl);

        // assert
        assertTrue(tenantFromIssUrl.isPresent());
        assertEquals(PITC, tenantFromIssUrl.get());
    }

    @DisplayName("Should return a input url if the url does not contain a slash after calling extractTenantFromIssUrl()")
    @Test
    void extractTenantFromIssUrlReturnsInputIfUrlNotContainSlash() {
        // arrange
        String issUrl = "this_is_not_a_valid_url";

        // act
        Optional<String> tenantFromIssUrl = UrlHelper.extractTenantFromIssUrl(issUrl);

        // assert
        assertTrue(tenantFromIssUrl.isPresent());
        assertEquals(issUrl, tenantFromIssUrl.get());
    }

    @DisplayName("Should return empty if the url is null after calling extractTenantFromIssUrl()")
    @Test
    void extractTenantFromIssUrlReturnsEmptyIfUrlIsNull() {
        // arrange
        String issUrl = null;

        // act
        Optional<String> tenantFromIssUrl = UrlHelper.extractTenantFromIssUrl(issUrl);

        // assert
        assertTrue(tenantFromIssUrl.isEmpty());
    }
}
