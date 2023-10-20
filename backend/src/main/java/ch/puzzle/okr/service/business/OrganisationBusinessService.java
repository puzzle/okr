package ch.puzzle.okr.service.business;

import com.unboundid.ldap.sdk.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OrganisationBusinessService {

    @Value("${ldapHost}")
    private String ldapHost;

    @Value("${ldapPort}")
    private int ldapPort;

    @Value("${bindDN}")
    private String bindDN;

    @Value("${bindPwd}")
    private String bindPwd;

    @Scheduled(cron = "0 0 0 * * ?") // This cron expression runs at midnight every day
    public void importOrgFromLDAP() {
        try (LDAPConnection connection = new LDAPConnection(ldapHost, ldapPort)) {
            connection.bind(bindDN, bindPwd);

            SearchRequest searchRequest = new SearchRequest("ou=groups,dc=puzzle,dc=itc", SearchScope.ONE,
                    Filter.create("(objectClass=groupOfNames)"), "cn");

            SearchResult searchResult = connection.search(searchRequest);

            for (SearchResultEntry entry : searchResult.getSearchEntries()) {
                String cn = entry.getAttributeValue("cn");
                if (cn != null && cn.startsWith("org_")) {
                    System.out.println(cn);
                }
            }
        } catch (LDAPException e) {
            throw new RuntimeException(e);
        }
    }
}
