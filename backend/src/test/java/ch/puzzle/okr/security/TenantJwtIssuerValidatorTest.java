package ch.puzzle.okr.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ch.puzzle.okr.multitenancy.TenantConfigProvider;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;

public class TenantJwtIssuerValidatorTest {

    private static final String PITC = "pitc";
    private static final String ISSUER_URL = "issuerUrl";

    @DisplayName("validate() throws exception if TenantConfig is not found for Tenant PITC")
    @Test
    void validateThrowsExceptionIfTenantConfigNotFound() {
        // arrange
        Jwt token = mock(Jwt.class);

        JwtHelper jwtHelper = mock(JwtHelper.class);
        when(jwtHelper.getTenantFromToken(token)).thenReturn(PITC);

        TenantConfigProvider emptyTenantConfigProvider = mock(TenantConfigProvider.class);

        // act + assert
        TenantJwtIssuerValidator validator = new TenantJwtIssuerValidator(emptyTenantConfigProvider, jwtHelper);
        IllegalArgumentException illegalArgumentException = //
                assertThrows(IllegalArgumentException.class, () -> validator.validate(token));

        assertEquals("unknown tenant", illegalArgumentException.getLocalizedMessage());
    }

    @DisplayName("validate() return OAuth2TokenValidatorResult if TenantConfig with IssuerUrl is found for Tenant PITC")
    @Test
    void validateReturnOAuth2TokenValidatorResultIfTenantConfigWithIssuerUrlIsFound() {
        // arrange
        Jwt token = mock(Jwt.class);

        JwtHelper jwtHelper = mock(JwtHelper.class);
        when(jwtHelper.getTenantFromToken(token)).thenReturn(PITC);

        TenantConfigProvider tenantConfigProviderWithPitcConfig = mock(TenantConfigProvider.class);
        when(tenantConfigProviderWithPitcConfig.getTenantConfigById(PITC))
                .thenReturn(Optional
                        .of( //
                            new TenantConfigProvider.TenantConfig( //
                                                                  PITC,
                                                                  new String[]{},
                                                                  "jwkSetUri", //
                                                                  ISSUER_URL,
                                                                  "clientId",
                                                                  null)));

        TenantJwtIssuerValidator tenantJwtIssuerValidator = new TenantJwtIssuerValidator(tenantConfigProviderWithPitcConfig,
                                                                                         jwtHelper) {

            @Override
            JwtIssuerValidator createValidator(String issuer) {
                OAuth2TokenValidatorResult validatorResultWithNoErrors = mock(OAuth2TokenValidatorResult.class);
                when(validatorResultWithNoErrors.hasErrors()).thenReturn(false);

                JwtIssuerValidator jwtIssuerValidator = mock(JwtIssuerValidator.class);
                when(jwtIssuerValidator.validate(any())).thenReturn(validatorResultWithNoErrors);
                return jwtIssuerValidator;
            }
        };

        // act
        OAuth2TokenValidatorResult validateResult = tenantJwtIssuerValidator.validate(token);

        // assert
        assertNotNull(validateResult);
        assertFalse(validateResult.hasErrors());
    }

}
