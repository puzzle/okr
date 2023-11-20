package ch.puzzle.okr.service.business;

import ch.puzzle.okr.mapper.CnAttributesMapper;
import ch.puzzle.okr.models.Organisation;
import ch.puzzle.okr.models.OrganisationState;
import ch.puzzle.okr.service.persistence.OrganisationPersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganisationBusinessServiceTest {

    private static final List<String> listOfOrgNames = new ArrayList<>();

    private static final List<Organisation> organisationDbMock = new ArrayList<>();

    private static final Organisation organisationOne = Organisation.Builder.builder().withId(1L).withOrgName("org_one")
            .withState(OrganisationState.ACTIVE).build();
    private static final Organisation organisationTwo = Organisation.Builder.builder().withId(2L).withOrgName("org_two")
            .withState(OrganisationState.ACTIVE).build();
    private static final Organisation organisationThree = Organisation.Builder.builder().withId(2L)
            .withOrgName("org_three").withState(OrganisationState.ACTIVE).build();

    private static final Organisation organisationInActive = Organisation.Builder.builder().withId(2L)
            .withOrgName("org_in_active").withState(OrganisationState.INACTIVE).build();

    @Mock
    LdapTemplate ldapTemplate;

    @Mock
    CnAttributesMapper cnAttributesMapper;

    @Mock
    OrganisationPersistenceService organisationPersistenceService;

    @InjectMocks
    @Spy
    OrganisationBusinessService organisationBusinessService;

    @BeforeEach
    void prepareLists() {
        listOfOrgNames.addAll(List.of("org_one", "org_two"));
        organisationDbMock.addAll(List.of(organisationOne, organisationTwo, organisationThree));
    }

    @Test
    void shouldRemoveEmptyEntriesOfList() {
        when(ldapTemplate.search(any(LdapQuery.class), any(CnAttributesMapper.class))).thenReturn(listOfOrgNames);
        when(organisationPersistenceService.findAll()).thenReturn(organisationDbMock);
        organisationBusinessService.importOrgFromLDAP();
        verify(organisationPersistenceService, atLeast(1)).saveIfNotExists(
                Organisation.Builder.builder().withOrgName("org_one").withState(OrganisationState.ACTIVE).build());
        verify(organisationPersistenceService, atLeast(1)).saveIfNotExists(
                Organisation.Builder.builder().withOrgName("org_two").withState(OrganisationState.ACTIVE).build());
        verify(organisationPersistenceService, atLeast(2)).saveIfNotExists(any());
        verify(organisationPersistenceService, atLeast(1)).updateOrganisationStateToInactive("org_three");
        verify(organisationPersistenceService, atLeast(1)).updateOrganisationStateToInactive(anyString());
    }

    @Test
    void getAllOrganisations() {
        organisationBusinessService.getActiveOrganisations();
        verify(organisationPersistenceService, times(1)).getActiveOrganisations();
    }

    @Test
    void getOrganisationsByTeamId() {
        organisationBusinessService.getOrganisationsByTeam(1L);
        verify(organisationPersistenceService, times(1)).getOrganisationsByTeamId(1L);
    }

    @Test
    void teamHasInActiveOrganisations() {
        when(organisationPersistenceService.getOrganisationsByTeamId(anyLong()))
                .thenReturn(List.of(organisationInActive, organisationOne, organisationTwo));
        assertTrue(organisationBusinessService.teamHasInActiveOrganisations(1L));
    }

    @Test
    void teamHasNoInActiveOrganisations() {
        when(organisationPersistenceService.getOrganisationsByTeamId(anyLong()))
                .thenReturn(List.of(organisationOne, organisationTwo, organisationThree));
        assertFalse(organisationBusinessService.teamHasInActiveOrganisations(1L));
    }
}
