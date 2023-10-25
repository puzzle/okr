package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Organisation;
import ch.puzzle.okr.models.OrganisationState;
import ch.puzzle.okr.service.persistence.OrganisationPersistenceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OrganisationBusinessService {

    private final OrganisationPersistenceService persistenceService;
    private final LdapTemplate ldapTemplate;
    @Value("${okr.jwt.claim.organisation.name.prefix}")
    private String organisationNamePrefix;

    public OrganisationBusinessService(LdapTemplate ldapTemplate, OrganisationPersistenceService persistenceService) {
        this.ldapTemplate = ldapTemplate;
        this.persistenceService = persistenceService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void importOrgFromLDAP() {
        LdapQuery query = LdapQueryBuilder.query().base("ou=groups").where("objectClass").is("groupOfNames");
        List<String> organisations = ldapTemplate.search(query, new CnAttributesMapper());
        organisations.removeIf(Objects::isNull);
        for (String org : organisations) {
            persistenceService.saveIfNotExists(
                    Organisation.Builder.builder().withOrgName(org).withState(OrganisationState.ACTIVE).build());
        }
        List<Organisation> existingOrganisations = persistenceService.findAll();
        List<String> existingOrganisationNames = new ArrayList<>(
                existingOrganisations.stream().map(Organisation::getOrgName).toList());
        existingOrganisationNames.removeAll(organisations);
        for (String orgName : existingOrganisationNames) {
            persistenceService.updateOrganisationStateToInactive(orgName);
        }
    }

    private class CnAttributesMapper implements AttributesMapper<String> {
        @Override
        public String mapFromAttributes(Attributes attributes) throws NamingException {
            Attribute cnAttribute = attributes.get("cn");
            if (cnAttribute != null && cnAttribute.get().toString().startsWith(organisationNamePrefix)) {
                return cnAttribute.get().toString();
            }
            return null;
        }
    }
}
