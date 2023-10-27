package ch.puzzle.okr.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

import static ch.puzzle.okr.TestHelper.defaultJwtToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.ReflectionTestUtils.setField;

class JwtOrganisationConverterTest {
    private final JwtOrganisationConverter converter = new JwtOrganisationConverter();
    private final Jwt jwt = defaultJwtToken();

    @BeforeEach
    public void setup() {
        setClaimRealm("pitc");
        setClaimOrganisations("roles");
        setOrganisationNamePrefix("org_");
    }

    @Test
    void convert_shouldReturnOrganisations_whenValidJwt() {
        List<String> organisations = converter.convert(jwt);

        assertThat(List.of("org_gl")).hasSameElementsAs(organisations);
    }

    @Test
    void convert_shouldReturnEmptyList_whenNoClaimRealmSection() {
        setClaimRealm("foo");

        List<String> organisations = converter.convert(jwt);

        assertThat(List.of()).hasSameElementsAs(organisations);
    }

    @Test
    void convert_shouldReturnEmptyList_whenNoClaimOrganisationsSection() {
        setClaimOrganisations("bar");

        List<String> organisations = converter.convert(jwt);

        assertThat(List.of()).hasSameElementsAs(organisations);
    }

    @Test
    void convert_shouldReturnEmptyList_whenNoRoleNameMatch() {
        setOrganisationNamePrefix("foo_");

        List<String> organisations = converter.convert(jwt);

        assertThat(List.of()).hasSameElementsAs(organisations);
    }

    private void setClaimRealm(String claimRealm) {
        setField(converter, "claimRealm", claimRealm);
    }

    private void setClaimOrganisations(String claimOrganisations) {
        setField(converter, "claimOrganisations", claimOrganisations);
    }

    private void setOrganisationNamePrefix(String roleNamePrefix) {
        setField(converter, "organisationNamePrefix", roleNamePrefix);
    }
}
