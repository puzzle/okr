package ch.puzzle.okr.service.authorization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

import static ch.puzzle.okr.TestHelper.defaultJwtToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.ReflectionTestUtils.setField;

class JwtRolesConverterTest {
    private final JwtRolesConverter converter = new JwtRolesConverter();
    private final Jwt jwt = defaultJwtToken();

    @BeforeEach
    public void setup() {
        setClaimRealm("pict");
        setClaimRoles("roles");
        setRoleNamePrefix("org_");
    }

    @Test
    void convert_shouldReturnRoles_whenValidJwt() {
        List<String> roleNames = converter.convert(jwt);

        assertThat(List.of("org_gl")).hasSameElementsAs(roleNames);
    }

    @Test
    void convert_shouldReturnEmptyList_whenNoClaimRealmSection() {
        setClaimRealm("foo");

        List<String> roleNames = converter.convert(jwt);

        assertThat(List.of()).hasSameElementsAs(roleNames);
    }

    @Test
    void convert_shouldReturnEmptyList_whenNoClaimRolesSection() {
        setClaimRoles("bar");

        List<String> roleNames = converter.convert(jwt);

        assertThat(List.of()).hasSameElementsAs(roleNames);
    }

    @Test
    void convert_shouldReturnEmptyList_whenNoRoleNameMatch() {
        setRoleNamePrefix("foo_");

        List<String> roleNames = converter.convert(jwt);

        assertThat(List.of()).hasSameElementsAs(roleNames);
    }

    private void setClaimRealm(String claimRealm) {
        setField(converter, "claimRealm", claimRealm);
    }

    private void setClaimRoles(String claimRoles) {
        setField(converter, "claimRoles", claimRoles);
    }

    private void setRoleNamePrefix(String roleNamePrefix) {
        setField(converter, "roleNamePrefix", roleNamePrefix);
    }
}
