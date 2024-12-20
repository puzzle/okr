package ch.puzzle.okr.security;

import ch.puzzle.okr.multitenancy.TenantConfigProvider;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TenantJWSKeySelectorTest {
    private static final String PITC = "pitc";
    private static final String JWK_SET_URI = "jwkSetUri";
    private static final String MOCK_ALGORITHM = "mock_algorithm";
    private static final String UNKNOWN_TENANT = "unknown tenant";

    @DisplayName("Should throw exception if the JwkSetUri was not found in the tenant-config-provider after calling selectKeys()")
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
                () -> selector.selectKeys(jwsHeaderMock, jwtClaimsSetMock, securityContext));

        assertEquals(UNKNOWN_TENANT, illegalArgumentException.getLocalizedMessage());
    }

    @DisplayName("Should return a key with the mock algorithm if the JwkSetUri is found in the tenant-config-provider after calling selectKeys()")
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
