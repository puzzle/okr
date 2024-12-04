package ch.puzzle.okr.security.helper;

import com.nimbusds.jwt.JWTClaimsSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.text.ParseException;
import java.util.Optional;

import static ch.puzzle.okr.security.JwtHelper.CLAIM_ISS;
import static ch.puzzle.okr.security.JwtHelper.CLAIM_TENANT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClaimHelperTest {

    private static final String PITC = "pitc";
    private ClaimHelper helper;

    @BeforeEach
    void setup() {
        helper = new ClaimHelper();
    }

    @DisplayName("getTenantFromClaimsSetUsingClaimTenant() returns tenant if claim tenant is found")
    @Test
    void getTenantFromClaimsSetUsingClaimTenantReturnsTenantIfClaimTenantFound() throws ParseException {
        // arrange
        JWTClaimsSet claimsSetMock = mock(JWTClaimsSet.class);
        when(claimsSetMock.getStringClaim(CLAIM_TENANT)).thenReturn(PITC);

        // act
        Optional<String> tenant = helper.getTenantFromClaimsSetUsingClaimTenant(claimsSetMock);

        // assert
        assertTrue(tenant.isPresent());
        assertEquals(PITC, tenant.get());
    }

    @DisplayName("getTenantFromClaimsSetUsingClaimTenant() return empty if claim tenant is not found")
    @Test
    void getTenantFromClaimsSetUsingClaimTenantReturnEmptyIfClaimTenantNotFound() {
        // arrange
        JWTClaimsSet claimsSetMock = mock(JWTClaimsSet.class);

        // act
        Optional<String> tenant = helper.getTenantFromClaimsSetUsingClaimTenant(claimsSetMock);

        // assert
        assertTrue(tenant.isEmpty());
    }

    @DisplayName("getTenantFromClaimsSetUsingClaimTenant() returns empty if claim tenant is found but value is null")
    @Test
    void getTenantFromClaimsSetUsingClaimTenantReturnsEmptyIfClaimTenantFoundButValueIsNull() throws ParseException {
        // arrange
        JWTClaimsSet claimsSetMock = mock(JWTClaimsSet.class);
        when(claimsSetMock.getStringClaim(CLAIM_TENANT)).thenReturn(null);

        // act
        Optional<String> tenant = helper.getTenantFromClaimsSetUsingClaimTenant(claimsSetMock);

        // assert
        assertTrue(tenant.isEmpty());
    }

    @DisplayName("getTenantFromClaimsSetUsingClaimTenant() returns empty if claim tenant is found but parsing of claim failed")
    @Test
    void getTenantFromClaimsSetUsingClaimTenantReturnsEmptyIfParsingOfClaimFailed() throws ParseException {
        // arrange
        JWTClaimsSet claimsSetMock = mock(JWTClaimsSet.class);
        when(claimsSetMock.getStringClaim(CLAIM_TENANT)).thenThrow(new ParseException("", 0));

        // act
        Optional<String> tenant = helper.getTenantFromClaimsSetUsingClaimTenant(claimsSetMock);

        // assert
        assertTrue(tenant.isEmpty());
    }

    @DisplayName("getTenantFromClaimsSetUsingClaimIss() returns tenant if claim iss is found")
    @ParameterizedTest
    @ValueSource(strings = { "https://sso.puzzle.ch/auth/realms/pitc", "http://localhost:8544/realms/pitc" })
    void getTenantFromClaimsSetUsingClaimIssReturnsTenantIfClaimIssFound(String issUrl) throws ParseException {
        // arrange
        JWTClaimsSet claimsSetMock = mock(JWTClaimsSet.class);
        when(claimsSetMock.getStringClaim(CLAIM_ISS)).thenReturn(issUrl);

        // act
        Optional<String> tenant = helper.getTenantFromClaimsSetUsingClaimIss(claimsSetMock);

        // assert
        assertTrue(tenant.isPresent());
        assertEquals(PITC, tenant.get());
    }

    @DisplayName("getTenantFromClaimsSetUsingClaimIss() returns empty if claim iss is not found")
    @Test
    void getTenantFromClaimsSetUsingClaimIssReturnsEmptyIfClaimIssNotFound() {
        // arrange
        JWTClaimsSet claimsSetMock = mock(JWTClaimsSet.class);

        // act
        Optional<String> tenant = helper.getTenantFromClaimsSetUsingClaimIss(claimsSetMock);

        // assert
        assertTrue(tenant.isEmpty());
    }

    @DisplayName("getTenantFromClaimsSetUsingClaimIss() returns empty if claim iss is found but value is null")
    @Test
    void getTenantFromClaimsSetUsingClaimIssReturnsEmptyIfClaimIssFoundButValueIsNull() throws ParseException {
        // arrange
        JWTClaimsSet claimsSetMock = mock(JWTClaimsSet.class);
        when(claimsSetMock.getStringClaim(CLAIM_ISS)).thenReturn(null);

        // act
        Optional<String> tenant = helper.getTenantFromClaimsSetUsingClaimIss(claimsSetMock);

        // assert
        assertTrue(tenant.isEmpty());
    }

    @DisplayName("getTenantFromClaimsSetUsingClaimIss() returns empty if parsing of claim failed")
    @Test
    void getTenantFromClaimsSetUsingClaimIssReturnsEmptyIfParsingOfClaimFailed() throws ParseException {
        // arrange
        JWTClaimsSet claimsSetMock = mock(JWTClaimsSet.class);
        when(claimsSetMock.getStringClaim(CLAIM_ISS)).thenThrow(new ParseException("", 0));

        // act
        Optional<String> tenant = helper.getTenantFromClaimsSetUsingClaimIss(claimsSetMock);

        // assert
        assertTrue(tenant.isEmpty());
    }

}
