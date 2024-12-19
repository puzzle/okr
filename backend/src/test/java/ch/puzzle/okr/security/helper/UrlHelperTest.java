package ch.puzzle.okr.security.helper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class UrlHelperTest {

    private static final String PITC = "pitc";

    @DisplayName("extractTenantFromIssUrl() return tenant if Url contains slash")
    @ParameterizedTest
    @ValueSource(strings = { "https://sso.puzzle.ch/auth/realms/pitc", "http://localhost:8544/realms/pitc" })
    void extractTenantFromIssUrlReturnTenantIfUrlContainSlash(String issUrl) {
        // arrange

        // act
        Optional<String> tenantFromIssUrl = UrlHelper.extractTenantFromIssUrl(issUrl);

        // assert
        assertTrue(tenantFromIssUrl.isPresent());
        assertEquals(PITC, tenantFromIssUrl.get());
    }

    @DisplayName("extractTenantFromIssUrl() return input url if url not contains slash")
    @Test
    void extractTenantFromIssUrlReturnInputIfUrlNotContainSlash() {
        // arrange
        String issUrl = "this_is_not_a_valid_url";

        // act
        Optional<String> tenantFromIssUrl = UrlHelper.extractTenantFromIssUrl(issUrl);

        // assert
        assertTrue(tenantFromIssUrl.isPresent());
        assertEquals(issUrl, tenantFromIssUrl.get());
    }

    @DisplayName("extractTenantFromIssUrl() return empty if url is null")
    @Test
    void extractTenantFromIssUrlReturnEmptyIfUrlIsNull() {
        // arrange
        String issUrl = null;

        // act
        Optional<String> tenantFromIssUrl = UrlHelper.extractTenantFromIssUrl(issUrl);

        // assert
        assertTrue(tenantFromIssUrl.isEmpty());
    }
}
