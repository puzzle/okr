package ch.puzzle.okr.service.business;

import static ch.puzzle.okr.test.OverviewTestHelper.QUARTER_ID;
import static ch.puzzle.okr.test.OverviewTestHelper.teamIds;
import static ch.puzzle.okr.test.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.models.overview.OverviewId;
import ch.puzzle.okr.service.persistence.OverviewPersistenceService;
import ch.puzzle.okr.service.validation.OverviewValidationService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class OverviewBusinessServiceTest {

    private static final AuthorizationUser authorizationUser = defaultAuthorizationUser();

    @InjectMocks
    OverviewBusinessService overviewBusinessService;

    @Mock
    OverviewPersistenceService overviewPersistenceService;

    @Mock
    QuarterBusinessService quarterBusinessService;

    @Mock
    OverviewValidationService overviewValidationService;

    private static List<Overview> createOverviews() {
        return createOverviews(authorizationUser);
    }

    private static Quarter normalQuarter = Quarter.Builder
            .builder()
            .withId(QUARTER_ID)
            .withLabel("GJ 22/23-Q2")
            .build();

    private static List<Overview> createOverviews(AuthorizationUser authorizationUser) {
        long index = 1L;
        List<Overview> overviews = new ArrayList<>(List
                .of(Overview.Builder
                        .builder()
                        .withOverviewId(OverviewId.Builder.builder().withObjectiveId(index++).withTeamId(111L).build())
                        .withObjectiveTitle("Another Team Objective A")
                        .withTeamName("team-111")
                        .withObjectiveCreatedOn(LocalDateTime.of(2023, 10, 21, 18, 33))
                        .withQuarterId(5L)
                        .build(),
                    Overview.Builder
                            .builder()
                            .withOverviewId(OverviewId.Builder
                                    .builder()
                                    .withObjectiveId(index++)
                                    .withTeamId(222L)
                                    .build())
                            .withObjectiveTitle("Another Team Objective B")
                            .withTeamName("team-222")
                            .withObjectiveCreatedOn(LocalDateTime.of(2023, 10, 1, 8, 53))
                            .withQuarterId(1L)
                            .build()));

        for (Long teamId : authorizationUser.extractTeamIds()) {
            overviews
                    .addAll((List
                            .of(Overview.Builder
                                    .builder()
                                    .withOverviewId(OverviewId.Builder
                                            .builder()
                                            .withObjectiveId(index++)
                                            .withTeamId(teamId)
                                            .build())
                                    .withObjectiveTitle("ZZZ Objective")
                                    .withTeamName("firstLevelTeam-" + teamId)
                                    .withObjectiveCreatedOn(LocalDateTime.of(2023, 12, 10, 18, 33))
                                    .build(),
                                Overview.Builder
                                        .builder()
                                        .withOverviewId(OverviewId.Builder
                                                .builder()
                                                .withObjectiveId(index++)
                                                .withTeamId(teamId)
                                                .build())
                                        .withObjectiveTitle("AAA Objective")
                                        .withTeamName("firstLevelTeam-" + teamId)
                                        .withObjectiveCreatedOn(LocalDateTime.of(2023, 9, 10, 18, 33))
                                        .build(),
                                Overview.Builder
                                        .builder()
                                        .withOverviewId(OverviewId.Builder
                                                .builder()
                                                .withObjectiveId(index++)
                                                .withTeamId(teamId)
                                                .build())
                                        .withObjectiveTitle("AAA Objective")
                                        .withTeamName("firstLevelTeam-" + teamId)
                                        .withObjectiveCreatedOn(LocalDateTime.of(2023, 9, 10, 18, 33))
                                        .build())));
        }
        return overviews;
    }

    @DisplayName("Should return correct list of overviews on getFilteredOverview()")
    @Test
    void shouldReturnListOfOverviewsUsingGetFilteredOverview() {
        when(overviewPersistenceService.getFilteredOverview(QUARTER_ID, teamIds, "Objective", authorizationUser))
                .thenReturn(createOverviews());
        when(quarterBusinessService.getQuarterById(any())).thenReturn(new Quarter());

        List<Overview> overviews = overviewBusinessService.getFilteredOverview(QUARTER_ID, teamIds, "Objective",
                authorizationUser);

        assertEquals(5, overviews.size());
        verify(overviewPersistenceService, times(1)).getFilteredOverview(QUARTER_ID, teamIds, "Objective",
                authorizationUser);
        verify(quarterBusinessService, times(0)).getCurrentQuarter();
        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewValidationService, times(1)).validateOnGet(QUARTER_ID, teamIds);
        verify(overviewPersistenceService, times(1)).getFilteredOverview(QUARTER_ID, teamIds, "Objective",
                authorizationUser);
    }

    @DisplayName("Should return overviews of current quarter on getFilteredOverview() when quarter id is null")
    @Test
    void shouldReturnListOfOverviewsWhenQuarterIsNullUsingGetFilteredOverview() {
        when(overviewPersistenceService.getFilteredOverview(QUARTER_ID, teamIds, "", authorizationUser))
                .thenReturn(createOverviews());
        when(quarterBusinessService.getCurrentQuarter())
                .thenReturn(normalQuarter);
        when(quarterBusinessService.getQuarterById(any()))
                .thenReturn(normalQuarter);

        List<Overview> overviews = overviewBusinessService.getFilteredOverview(null, teamIds, "", authorizationUser);

        assertEquals(5, overviews.size());

        verify(quarterBusinessService, times(1)).getCurrentQuarter();
        verify(overviewValidationService, times(1)).validateOnGet(QUARTER_ID, teamIds);
        verify(overviewPersistenceService, times(1)).getFilteredOverview(QUARTER_ID, teamIds, "", authorizationUser);
    }

    @DisplayName("Should return empty list of overviews on getFilteredOverview() when team ids are null")
    @Test
    void shouldReturnEmptyListOfOverviewsWhenTeamIdsAreNullUsingGetFilteredOverview() {
        List<Overview> overviews = overviewBusinessService.getFilteredOverview(QUARTER_ID, null, "", authorizationUser);

        assertEquals(0, overviews.size());
        verify(overviewValidationService, times(1)).validateOnGet(QUARTER_ID, List.of());
        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewPersistenceService, never()).getFilteredOverview(QUARTER_ID, List.of(), "", authorizationUser);
        verify(overviewPersistenceService, never())
                .getFilteredOverview(anyLong(), anyList(), anyString(), eq(authorizationUser));
    }

    @DisplayName("Should throw exception on getFilteredOverview() when quarter id does not exist")
    @Test
    void shouldThrowNotFoundWhenQuarterIdIsNonExistentUsingGetFilteredOverview() {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
                .when(overviewValidationService)
                .validateOnGet(eq(QUARTER_ID), anyList());

        List<Long> emptyTeamIdsList = List.of();
        assertThrows(ResponseStatusException.class,
                     () -> overviewBusinessService
                             .getFilteredOverview(QUARTER_ID, emptyTeamIdsList, "", authorizationUser));

        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewValidationService, never()).validateOnGet(QUARTER_ID, teamIds);
        verify(overviewPersistenceService, never())
                .getFilteredOverview(anyLong(), anyList(), anyString(), eq(authorizationUser));
    }

    @DisplayName("Should throw exception on getFilteredOverview() when team id does not exist")
    @Test
    void shouldThrowNotFoundWhenTeamIdIsNonExistentUsingGetFilteredOverview() {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
                .when(overviewValidationService)
                .validateOnGet(QUARTER_ID, teamIds);

        assertThrows(ResponseStatusException.class,
                     () -> overviewBusinessService.getFilteredOverview(QUARTER_ID, teamIds, "", authorizationUser));

        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewValidationService, never()).validateQuarter(QUARTER_ID);
        verify(overviewValidationService, times(1)).validateOnGet(QUARTER_ID, teamIds);
        verify(overviewPersistenceService, never())
                .getFilteredOverview(anyLong(), anyList(), any(), eq(authorizationUser));
    }

    @DisplayName("Should return sorted list with teams that include the user first on getFilteredOverview()")
    @Test
    void shouldReturnSortedListWithUserTeamsFirstUsingGetFilteredOverview() {
        AuthorizationUser user = mockAuthorizationUser(defaultUser(13L));
        when(overviewPersistenceService.getFilteredOverview(QUARTER_ID, teamIds, null, user))
                .thenReturn(createOverviews(user));
        when(quarterBusinessService.getQuarterById(any())).thenReturn(new Quarter());

        List<Overview> overviews = overviewBusinessService.getFilteredOverview(QUARTER_ID, teamIds, null, user);

        assertThat(List
                .of(OverviewId.of(1L, 4L, null, null),
                    OverviewId.of(1L, 5L, null, null),
                    OverviewId.of(1L, 3L, null, null),
                    OverviewId.of(111L, 1L, null, null),
                    OverviewId.of(222L, 2L, null, null)))
                .hasSameElementsAs(getOverviewIds(overviews));
    }

    private List<OverviewId> getOverviewIds(List<Overview> overviews) {
        return overviews.stream().map(Overview::getOverviewId).toList();
    }

    @Test
    void shouldSetQuarterAsBacklogQuarter() {
        when(overviewPersistenceService.getFilteredOverview(5L, teamIds, "", authorizationUser))
                .thenReturn(createOverviews().subList(0,2));
        when(quarterBusinessService.getQuarterById(5L))
                .thenReturn(Quarter.Builder.builder().withId(5L).withLabel("Backlog").withStartDate(null).withEndDate(null).build());
        when(quarterBusinessService.getQuarterById(1L))
                .thenReturn(Quarter.Builder.builder().withId(1L).withLabel("GJ 22/23-Q2").withStartDate(LocalDate.now()).withEndDate(LocalDate.of(3000, 10, 23)).build());

        List<Overview> overviewList = overviewBusinessService.getFilteredOverview(5L, teamIds, "", authorizationUser);

        assertTrue(overviewList.getFirst().isBacklogQuarter());
        assertFalse(overviewList.getLast().isBacklogQuarter());
    }
}
