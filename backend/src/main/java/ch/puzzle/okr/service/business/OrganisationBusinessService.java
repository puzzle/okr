package ch.puzzle.okr.service.business;

import ch.puzzle.okr.mapper.CnAttributesMapper;
import ch.puzzle.okr.models.Organisation;
import ch.puzzle.okr.models.OrganisationState;
import ch.puzzle.okr.service.persistence.OrganisationPersistenceService;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OrganisationBusinessService {

    private final OrganisationPersistenceService persistenceService;
    private final LdapTemplate ldapTemplate;

    private final CnAttributesMapper cnAttributesMapper;

    public OrganisationBusinessService(LdapTemplate ldapTemplate, OrganisationPersistenceService persistenceService,
            CnAttributesMapper cnAttributesMapper) {
        this.ldapTemplate = ldapTemplate;
        this.persistenceService = persistenceService;
        this.cnAttributesMapper = cnAttributesMapper;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void importOrgFromLDAP() {
        LdapQuery query = LdapQueryBuilder.query().base("ou=groups").where("objectClass").is("groupOfNames");
        List<String> organisations = ldapTemplate.search(query, cnAttributesMapper);
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
}
