package ch.puzzle.okr.security.helper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

import static ch.puzzle.okr.security.JwtHelper.CLAIM_ISS;
import static ch.puzzle.okr.security.JwtHelper.CLAIM_TENANT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokenHelperTest {

    private static final String PITC = "pitc";
    private TokenHelper helper;

    @BeforeEach
    void setUp() {
        helper = new TokenHelper();
    }

    @DisplayName("getTenantFromTokenUsingClaimTenant() returns tenant if claim tenant is found")
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

    @DisplayName("getTenantFromTokenUsingClaimTenant() returns empty if claim tenant not found")
    @Test
    void getTenantFromTokenUsingClaimTenantReturnsEmptyIfClaimTenantNotFound() {
        // arrange
        Jwt tokenMock = mock(Jwt.class);

        // act
        Optional<String> tenant = helper.getTenantFromTokenUsingClaimTenant(tokenMock);

        // assert
        assertTrue(tenant.isEmpty());
    }

    @DisplayName("getTenantFromTokenUsingClaimTenant() returns empty if claim tenant is found but value is null")
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

    @DisplayName("getTenantFromTokenUsingClaimIss() returns tenant if claim iss is found")
    @ParameterizedTest
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

    @DisplayName("getTenantFromTokenUsingClaimIss() returns empty if claim iss is not found")
    @Test
    void getTenantFromTokenUsingClaimIssReturnsEmptyIfClaimIssNotFound() {
        // arrange
        Jwt tokenMock = mock(Jwt.class);

        // act
        Optional<String> tenant = helper.getTenantFromTokenUsingClaimIss(tokenMock);

        // assert
        assertTrue(tenant.isEmpty());
    }

    @DisplayName("getTenantFromTokenUsingClaimIss() returns empty if claim iss is found but value is null")
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
