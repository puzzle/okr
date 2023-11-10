package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.OrganisationDto;
import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.mapper.OrganisationMapper;
import ch.puzzle.okr.models.Organisation;
import ch.puzzle.okr.models.OrganisationState;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.authorization.OrganisationAuthorizationService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(OrganisationController.class)
class OrganisationControllerIT {

    /* Team test objects */
    private static final Team PUZZLE = Team.Builder.builder().withId(1L).withName("PUZZLE ITC").build();
    private static final Team BBT = Team.Builder.builder().withId(1L).withName("/BBT").build();

    /* Organisation test objects */
    private static final Organisation organisationPuzzle = Organisation.Builder.builder().withId(1L)
            .withState(OrganisationState.ACTIVE).withTeams(List.of(PUZZLE)).withVersion(1).withOrgName("org_puzzle")
            .build();

    private static final Organisation organisationBBT = Organisation.Builder.builder().withId(1L)
            .withState(OrganisationState.ACTIVE).withTeams(List.of(BBT)).withVersion(1).withOrgName("org_bbt").build();

    private static final OrganisationDto organisationPuzzleDto = new OrganisationDto(1L, 1, "org_puzzle",
            OrganisationState.ACTIVE);

    private static final OrganisationDto organisationBBTDto = new OrganisationDto(1L, 1, "org_bbt",
            OrganisationState.ACTIVE);
    private static final String URL_ORGANISATION = "/api/v1/organisations";
    @Autowired
    private MockMvc mvc;
    @MockBean
    private OrganisationAuthorizationService organisationAuthorizationService;
    @MockBean
    private OrganisationMapper organisationMapper;

    @BeforeEach
    void setUp() {
        BDDMockito.given(organisationMapper.toDto(organisationPuzzle)).willReturn(organisationPuzzleDto);
        BDDMockito.given(organisationMapper.toDto(organisationBBT)).willReturn(organisationBBTDto);
    }

    @Test
    void shouldReturnOrganisationsOfBusinessService() throws Exception {
        BDDMockito.given(organisationAuthorizationService.getEntities(null))
                .willReturn(List.of(organisationPuzzle, organisationBBT));

        mvc.perform(get(URL_ORGANISATION).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].orgName", Is.is(organisationPuzzleDto.orgName())))
                .andExpect(jsonPath("$[1].orgName", Is.is(organisationBBTDto.orgName())));
    }
}
