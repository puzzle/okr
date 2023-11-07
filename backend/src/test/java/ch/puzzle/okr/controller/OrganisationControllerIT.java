package ch.puzzle.okr.controller;

import ch.puzzle.okr.models.Organisation;
import ch.puzzle.okr.models.OrganisationState;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.authorization.OrganisationAuthorizationService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(OrganisationController.class)
class OrganisationControllerIT {
    private static final Team PUZZLE = Team.Builder.builder().withId(1L).withName("PUZZLE ITC").build();
    private static final Organisation organisationPuzzle = Organisation.Builder.builder().withId(1L)
            .withState(OrganisationState.ACTIVE).withTeams(List.of(PUZZLE)).withVersion(1).withOrgName("org_puzzle")
            .build();

    private static final Organisation organisationBBT = Organisation.Builder.builder().withId(1L)
            .withState(OrganisationState.ACTIVE).withTeams(List.of(PUZZLE)).withVersion(1).withOrgName("org_bbt")
            .build();
    private static final String URL_ORGANISATION = "/api/v2/organisations";
    @Autowired
    private MockMvc mvc;
    @MockBean
    private OrganisationAuthorizationService organisationAuthorizationService;

    @Test
    void shouldReturnIsOk() throws Exception {
        mvc.perform(get(URL_ORGANISATION).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldReturnOrganisationsOfBusinessService() throws Exception {
        BDDMockito.given(organisationAuthorizationService.getEntities())
                .willReturn(List.of(organisationPuzzle, organisationBBT));
        mvc.perform(get(URL_ORGANISATION).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Is.is(organisationPuzzle)));
    }
}
