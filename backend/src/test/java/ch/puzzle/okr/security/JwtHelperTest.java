package ch.puzzle.okr.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.multitenancy.TenantConfigProvider;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.persistence.EntityNotFoundException;
import java.text.ParseException;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

public class JwtHelperTest {

    private static final String TOKEN_CLAIMS_KEY_FIRSTNAME = "given_name";
    private static final String TOKEN_CLAIMS_KEY_LASTNAME = "family_name";
    private static final String TOKEN_CLAIMS_KEY_EMAIL = "email";
    private static final String HANS = "Hans";
    private static final String MUSTER = "Muster";
    private static final String EMAIL = "a@b.ch";

    private static final String TOKEN_CLAIMS_KEY_TENANT = "tenant";
    private static final String PITC = "pitc";

    // ok
    @DisplayName("getUserFromJwt() extracts User data from Token")
    @Test
    void getUserFromJwtExtractsUserDataFromToken() {
        // arrange
        Jwt tokenWithUserDataMock = mock(Jwt.class);
        when(tokenWithUserDataMock.getClaims())
                .thenReturn(Map
                        .of( //
                            TOKEN_CLAIMS_KEY_FIRSTNAME,
                            HANS, //
                            TOKEN_CLAIMS_KEY_LASTNAME,
                            MUSTER, //
                            TOKEN_CLAIMS_KEY_EMAIL,
                            EMAIL //
                        ));

        JwtHelper jwtHelper = new JwtHelper(null, //
                                            TOKEN_CLAIMS_KEY_FIRSTNAME,
                                            TOKEN_CLAIMS_KEY_LASTNAME,
                                            TOKEN_CLAIMS_KEY_EMAIL);

        // act
        User userFromToken = jwtHelper.getUserFromJwt(tokenWithUserDataMock);

        // assert
        assertEquals(HANS, userFromToken.getFirstname());
        assertEquals(MUSTER, userFromToken.getLastname());
        assertEquals(EMAIL, userFromToken.getEmail());
    }

    // ok
    @DisplayName("getUserFromJwt() throws Exception if Token not contains User data")
    @Test
    void getUserFromJwtThrowsExceptionIfTokenNotContainsUserData() {
        // arrange
        Jwt tokenWithNoUserDataMock = mock(Jwt.class);

        JwtHelper jwtHelper = new JwtHelper(null, //
                                            TOKEN_CLAIMS_KEY_FIRSTNAME,
                                            TOKEN_CLAIMS_KEY_LASTNAME,
                                            TOKEN_CLAIMS_KEY_EMAIL);

        // act + assert
        OkrResponseStatusException okrResponseStatusException = //
                assertThrows(OkrResponseStatusException.class, () -> jwtHelper.getUserFromJwt(tokenWithNoUserDataMock));

        // assert
        assertEquals(BAD_REQUEST, okrResponseStatusException.getStatusCode());
    }

    @DisplayName("getTenantFromToken() returns Tenant if Tenant found in TenantConfigProvider")
    @Test
    void getTenantFromTokenReturnsTenantIfTenantFoundInTenantConfigProvider() {
        // arrange
        Jwt tokenMock = mock(Jwt.class);
        when(tokenMock.getClaimAsString(TOKEN_CLAIMS_KEY_TENANT)).thenReturn(PITC);

        TenantConfigProvider tenantConfigProviderMock = mock(TenantConfigProvider.class);
        when(tenantConfigProviderMock.getTenantConfigById(PITC))
                .thenReturn(Optional
                        .of( //
                            new TenantConfigProvider.TenantConfig(PITC, //
                                                                  new String[]{},
                                                                  "jwkSetUri",
                                                                  "issuerUrl", //
                                                                  "clientId",
                                                                  null) //
                        ));

        JwtHelper jwtHelper = new JwtHelper(tenantConfigProviderMock, null, null, null);

        // act
        String tenantFromToken = jwtHelper.getTenantFromToken(tokenMock);

        // assert
        assertEquals(PITC, tenantFromToken);
    }

    @DisplayName("getTenantFromToken() throws Exception if Tenant not found in TenantConfigProvider")
    @Test
    void getTenantFromTokenThrowsExceptionIfTenantNotFoundInTenantConfigProvider() {
        // arrange
        Jwt tokenMock = mock(Jwt.class);
        when(tokenMock.getClaimAsString(TOKEN_CLAIMS_KEY_TENANT)).thenReturn(PITC);

        TenantConfigProvider emptyTenantConfigProviderMock = mock(TenantConfigProvider.class);

        JwtHelper jwtHelper = new JwtHelper(emptyTenantConfigProviderMock, null, null, null);

        // act + assert
        assertThrows(EntityNotFoundException.class, () -> jwtHelper.getTenantFromToken(tokenMock));
    }

    @DisplayName("getTenantFromJWTClaimsSet() returns Tenant if Tenant found in TenantConfigProvider")
    @Test
    void getTenantFromJWTClaimsSetReturnsTenantIfTenantFoundInTenantConfigProvider() throws ParseException {
        // arrange
        JWTClaimsSet claimsSetMock = mock(JWTClaimsSet.class);
        when(claimsSetMock.getStringClaim(TOKEN_CLAIMS_KEY_TENANT)).thenReturn(PITC);

        TenantConfigProvider tenantConfigProviderWithDataMock = mock(TenantConfigProvider.class);
        when(tenantConfigProviderWithDataMock.getTenantConfigById(PITC))
                .thenReturn(Optional
                        .of( //
                            new TenantConfigProvider.TenantConfig(PITC, //
                                                                  new String[]{},
                                                                  "jwkSetUri",
                                                                  "issuerUrl", //
                                                                  "clientId",
                                                                  null) //
                        ));

        JwtHelper jwtHelper = new JwtHelper(tenantConfigProviderWithDataMock, null, null, null);

        // act
        String tenantFromToken = jwtHelper.getTenantFromJWTClaimsSet(claimsSetMock);

        // assert
        assertEquals(PITC, tenantFromToken);
    }

    @DisplayName("getTenantFromJWTClaimsSet() throws Exception if ClaimSet can not be parsed")
    @Test
    void getTenantFromJWTClaimsSetThrowsExceptionIfClaimSetCanNotBeParsed() throws ParseException {
        // arrange
        JWTClaimsSet claimsSetMock = mock(JWTClaimsSet.class);
        when(claimsSetMock.getStringClaim(TOKEN_CLAIMS_KEY_TENANT)).thenThrow(new ParseException("", 0));

        JwtHelper jwtHelper = new JwtHelper(null, null, null, null);

        // act + assert
        assertThrows(RuntimeException.class, () -> jwtHelper.getTenantFromJWTClaimsSet(claimsSetMock));
    }
}
