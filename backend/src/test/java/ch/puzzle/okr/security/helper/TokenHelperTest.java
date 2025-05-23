package ch.puzzle.okr.security.helper;

import static ch.puzzle.okr.security.JwtHelper.CLAIM_ISS;
import static ch.puzzle.okr.security.JwtHelper.CLAIM_TENANT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.oauth2.jwt.Jwt;

class TokenHelperTest {

    private static final String PITC = "pitc";
    private TokenHelper helper;

    @BeforeEach
    void setUp() {
        helper = new TokenHelper();
    }

    @DisplayName("Should return tenant if claim tenant is found after calling getTenantFromTokenUsingClaimTenant()")
    @Test
    void getTenantFromTokenUsingClaimTenantReturnsTenantIfClaimTenantFound() {
        // arrange
        Jwt tokenMock = mock(Jwt.class);
        when(tokenMock.getClaimAsString(CLAIM_TENANT)).thenReturn(PITC);

        // act
        Optional<String> tenant = helper.getTenantFromTokenUsingClaimTenant(tokenMock);

        // assert
        assertTrue(tenant.isPresent());
        assertEquals(PITC, tenant.get());
    }

    @DisplayName("Should return empty if claim tenant is not found after calling getTenantFromTokenUsingClaimTenant()")
    @Test
    void getTenantFromTokenUsingClaimTenantReturnsEmptyIfClaimTenantNotFound() {
        // arrange
        Jwt tokenMock = mock(Jwt.class);

        // act
        Optional<String> tenant = helper.getTenantFromTokenUsingClaimTenant(tokenMock);

        // assert
        assertTrue(tenant.isEmpty());
    }

    @DisplayName("Should return empty if claim tenant is found but value is null after calling getTenantFromTokenUsingClaimTenant()")
    @Test
    void getTenantFromTokenUsingClaimTenantReturnsEmptyIfClaimTenantFoundButValueIsNull() {
        // arrange
        Jwt tokenMock = mock(Jwt.class);
        when(tokenMock.getClaimAsString(CLAIM_TENANT)).thenReturn(null);

        // act
        Optional<String> tenant = helper.getTenantFromTokenUsingClaimTenant(tokenMock);

        // assert
        assertTrue(tenant.isEmpty());
    }

    @ParameterizedTest(name = "getTenantFromTokenUsingClaimIss() returns tenant if claim iss is found")
    @ValueSource(strings = { "https://sso.puzzle.ch/auth/realms/pitc", "http://localhost:8544/realms/pitc" })
    void getTenantFromTokenUsingClaimIssReturnsTenantIfClaimIssFound(String issUrl) {
        // arrange
        Jwt tokenMock = mock(Jwt.class);
        when(tokenMock.getClaimAsString(CLAIM_ISS)).thenReturn(issUrl);

        // act
        Optional<String> tenant = helper.getTenantFromTokenUsingClaimIss(tokenMock);

        // assert
        assertTrue(tenant.isPresent());
        assertEquals(PITC, tenant.get());
    }

    @DisplayName("Should return empty if claim iss is not found after calling getTenantFromTokenUsingClaimIss()")
    @Test
    void getTenantFromTokenUsingClaimIssReturnsEmptyIfClaimIssNotFound() {
        // arrange
        Jwt tokenMock = mock(Jwt.class);

        // act
        Optional<String> tenant = helper.getTenantFromTokenUsingClaimIss(tokenMock);

        // assert
        assertTrue(tenant.isEmpty());
    }

    @DisplayName("Should return empty if claim iss is found but value is null after calling getTenantFromTokenUsingClaimIss()")
    @Test
    void getTenantFromTokenUsingClaimIssReturnsEmptyIfClaimIssFoundButValueIsNull() {
        // arrange
        Jwt tokenMock = mock(Jwt.class);
        when(tokenMock.getClaimAsString(CLAIM_ISS)).thenReturn(null);

        // act
        Optional<String> tenant = helper.getTenantFromTokenUsingClaimIss(tokenMock);

        // assert
        assertTrue(tenant.isEmpty());
    }

}
