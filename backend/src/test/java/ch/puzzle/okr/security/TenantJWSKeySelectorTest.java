package ch.puzzle.okr.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ch.puzzle.okr.multitenancy.TenantConfigProvider;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import java.security.Key;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TenantJWSKeySelectorTest {
    private static final String PITC = "pitc";
    private static final String JWK_SET_URI = "jwkSetUri";
    private static final String MOCK_ALGORITHM = "mock_algorithm";
    private static final String UNKNOWN_TENANT = "unknown tenant";

    @DisplayName("selectKeys() throws Exception if JwkSetUri not found in TenantConfigProvider")
    @Test
    void selectKeysThrowsExceptionIfTenantConfigIsNotFound() {
        // arrange
        JWTClaimsSet jwtClaimsSetMock = mock(JWTClaimsSet.class);
        JWSHeader jwsHeaderMock = mock(JWSHeader.class);
        SecurityContext securityContext = new SecurityContext() {
        };

        JwtHelper jwtHelperMock = mock(JwtHelper.class);
        when(jwtHelperMock.getTenantFromJWTClaimsSet(jwtClaimsSetMock)).thenReturn(PITC);

        TenantConfigProvider emptyTenantConfigProviderMock = mock(TenantConfigProvider.class);

        // act + assert
        TenantJWSKeySelector selector = new TenantJWSKeySelector(emptyTenantConfigProviderMock, jwtHelperMock);
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, //
                                                                         () -> selector
                                                                                 .selectKeys(jwsHeaderMock,
                                                                                             jwtClaimsSetMock,
                                                                                             securityContext));

        assertEquals(UNKNOWN_TENANT, illegalArgumentException.getLocalizedMessage());
    }

    @DisplayName("selectKeys() return Key with Mock Algorithm if JwkSetUri is found in TenantConfigProvider")
    @Test
    void selectKeysReturnKeyWithMockAlgorithmIfJwkSetUriIsFound() throws KeySourceException {
        // arrange
        JWTClaimsSet jwtClaimsSetMock = mock(JWTClaimsSet.class);
        JWSHeader jwsHeaderMock = mock(JWSHeader.class);
        SecurityContext securityContext = new SecurityContext() {
        };

        JwtHelper jwtHelperMock = mock(JwtHelper.class);
        when(jwtHelperMock.getTenantFromJWTClaimsSet(jwtClaimsSetMock)).thenReturn(PITC);

        TenantConfigProvider tenantConfigProviderWithDataMock = mock(TenantConfigProvider.class);
        when(tenantConfigProviderWithDataMock.getJwkSetUri(PITC)).thenReturn(Optional.of(JWK_SET_URI));

        TenantJWSKeySelector selector = new TenantJWSKeySelector(tenantConfigProviderWithDataMock, jwtHelperMock) {

            @Override
            JWSKeySelector<SecurityContext> fromUri(String uri) {
                return (jwsHeader, securityContext) -> List.of(new Key() {
                    @Override
                    public String getAlgorithm() {
                        return MOCK_ALGORITHM;
                    }

                    @Override
                    public String getFormat() {
                        return null;
                    }

                    @Override
                    public byte[] getEncoded() {
                        return new byte[0];
                    }
                });
            }
        };

        // act + assert
        List<? extends Key> keys = selector.selectKeys(jwsHeaderMock, jwtClaimsSetMock, securityContext);

        // assert
        assertListContainsSingleKeyWithMockAlgorithm(keys);
    }

    void assertListContainsSingleKeyWithMockAlgorithm(List<? extends Key> keys) {
        assertNotNull(keys);
        assertEquals(1, keys.size());
        Key key = keys.get(0);
        assertEquals(MOCK_ALGORITHM, key.getAlgorithm());
    }
}
