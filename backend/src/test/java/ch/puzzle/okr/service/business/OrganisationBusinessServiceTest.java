package ch.puzzle.okr.service.business;

import org.junit.jupiter.api.Test;

public class OrganisationBusinessServiceTest {

    private OrganisationBusinessService service;

    public OrganisationBusinessServiceTest(OrganisationBusinessService service) {
        this.service = service;
    }

    @Test
    public void testLdap() {
        service.importOrgFromLDAP();
    }

}
