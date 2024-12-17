package ch.puzzle.okr.security.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UrlHelperTest {

    private static final String PITC = "pitc";

    @DisplayName("extractTenantFromIssUrl() returns tenant if Url contains slash")
    @ParameterizedTest
    @ValueSource(strings = { "https://sso.puzzle.ch/auth/realms/pitc", "http://localhost:8544/realms/pitc" })
    void extractTenantFromIssUrlReturnsTenantIfUrlContainSlash(String issUrl) {
        // arrange

        // act
        Optional<String> tenantFromIssUrl = UrlHelper.extractTenantFromIssUrl(issUrl);

        // assert
        assertTrue(tenantFromIssUrl.isPresent());
        assertEquals(PITC, tenantFromIssUrl.get());
    }

    @DisplayName("extractTenantFromIssUrl() returns input url if url does not contain slash")
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

    @DisplayName("extractTenantFromIssUrl() returns empty if url is null")
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
