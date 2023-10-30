package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.models.overview.OverviewId;
import ch.puzzle.okr.service.persistence.OverviewPersistenceService;
import ch.puzzle.okr.service.validation.OverviewValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ch.puzzle.okr.OverviewTestHelper.QUARTER_ID;
import static ch.puzzle.okr.OverviewTestHelper.teamIds;
import static ch.puzzle.okr.TestHelper.*;
import static ch.puzzle.okr.models.authorization.AuthorizationRole.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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

    @Mock
    TeamBusinessService teamBusinessService;

    private static List<Overview> createOverviews() {
        return createOverviews(authorizationUser);
    }

    private static List<Overview> createOverviews(AuthorizationUser user, List<Long> teamIds) {
        List<Overview> overviews = createOverviews(user);
        long index = 151L;
        for (Long teamId : teamIds) {
            overviews.addAll((List.of(
                    Overview.Builder.builder()
                            .withOverviewId(OverviewId.Builder.builder().withObjectiveId(51L).withTeamId(teamId)
                                    .withKeyResultId(index).build())
                            .withObjectiveTitle("ZZZ Objective").withTeamName("team-" + teamId)
                            .withObjectiveCreatedOn(LocalDateTime.of(2023, 9, 12, 17, 19))
                            .withKeyResultTitle("keyResult-" + index++).build(),
                    Overview.Builder.builder()
                            .withOverviewId(OverviewId.Builder.builder().withObjectiveId(51L).withTeamId(teamId)
                                    .withKeyResultId(index).build())
                            .withObjectiveTitle("ZZZ Objective").withTeamName("team-" + teamId)
                            .withObjectiveCreatedOn(LocalDateTime.of(2023, 9, 12, 17, 19))
                            .withKeyResultTitle("keyResult-" + index++).build())));
        }
        return overviews;
    }

    private static List<Overview> createOverviews(AuthorizationUser user) {
        long index = 1L;
        List<Overview> overviews = new ArrayList<>();
        for (Long teamId : user.firstLevelTeamIds()) {
            overviews
                    .addAll((List.of(
                            Overview.Builder.builder()
                                    .withOverviewId(OverviewId.Builder.builder().withObjectiveId(index++)
                                            .withTeamId(teamId).build())
                                    .withObjectiveTitle("ZZZ Objective").withTeamName("firstLevelTeam-" + teamId)
                                    .withObjectiveCreatedOn(LocalDateTime.of(2023, 12, 10, 18, 33)).build(),
                            Overview.Builder.builder()
                                    .withOverviewId(OverviewId.Builder.builder().withObjectiveId(index++)
                                            .withTeamId(teamId).build())
                                    .withObjectiveTitle("AAA Objective").withTeamName("firstLevelTeam-" + teamId)
                                    .withObjectiveCreatedOn(LocalDateTime.of(2023, 9, 10, 18, 33)).build(),
                            Overview.Builder.builder()
                                    .withOverviewId(OverviewId.Builder.builder().withObjectiveId(index++)
                                            .withTeamId(teamId).build())
                                    .withObjectiveTitle("AAA Objective").withTeamName("firstLevelTeam-" + teamId)
                                    .withObjectiveCreatedOn(LocalDateTime.of(2023, 9, 10, 18, 33)).build())));
        }
        for (Long teamId : user.userTeamIds()) {
            overviews
                    .addAll(List.of(
                            Overview.Builder.builder()
                                    .withOverviewId(OverviewId.Builder.builder().withObjectiveId(index++)
                                            .withTeamId(teamId).build())
                                    .withObjectiveTitle("CCC Objective").withTeamName("team-" + teamId)
                                    .withObjectiveCreatedOn(LocalDateTime.of(2023, 10, 21, 18, 33)).build(),
                            Overview.Builder.builder()
                                    .withOverviewId(OverviewId.Builder.builder().withObjectiveId(index++)
                                            .withTeamId(teamId).build())
                                    .withObjectiveTitle("BBB Objective").withTeamName("team-" + teamId)
                                    .withObjectiveCreatedOn(LocalDateTime.of(2023, 10, 1, 8, 53)).build()));
        }
        return overviews;
    }

    @Test
    void getFilteredOverviewShouldReturnListOfOverviews() {
        when(overviewPersistenceService.getFilteredOverview(QUARTER_ID, teamIds, "Objective", authorizationUser))
                .thenReturn(createOverviews());

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

    @Test
    void getFilteredOverviewShouldReturnListOfOverviewsWhenQuarterIsNull() {
        when(overviewPersistenceService.getFilteredOverview(QUARTER_ID, teamIds, "", authorizationUser))
                .thenReturn(createOverviews());
        when(quarterBusinessService.getCurrentQuarter())
                .thenReturn(Quarter.Builder.builder().withId(QUARTER_ID).withLabel("GJ 22/23-Q2").build());

        List<Overview> overviews = overviewBusinessService.getFilteredOverview(null, teamIds, "", authorizationUser);

        assertEquals(5, overviews.size());

        verify(quarterBusinessService, times(1)).getCurrentQuarter();
        verify(overviewValidationService, times(1)).validateOnGet(QUARTER_ID, teamIds);
        verify(overviewPersistenceService, times(1)).getFilteredOverview(QUARTER_ID, teamIds, "", authorizationUser);
    }

    @Test
    void getFilteredOverviewShouldReturnListOfOverviewsWhenTeamIdsAreNull() {
        when(overviewPersistenceService.getFilteredOverview(QUARTER_ID, List.of(5L), "", authorizationUser))
                .thenReturn(createOverviews());

        when(teamBusinessService.getAllTeams()).thenReturn(List.of(Team.Builder.builder().withId(5L).build()));

        List<Overview> overviews = overviewBusinessService.getFilteredOverview(QUARTER_ID, null, "", authorizationUser);

        assertEquals(5, overviews.size());
        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewPersistenceService, times(1)).getFilteredOverview(QUARTER_ID, List.of(5L), "",
                authorizationUser);
        verify(overviewValidationService, times(1)).validateOnGet(QUARTER_ID, List.of(5L));
        verify(overviewPersistenceService, times(1)).getFilteredOverview(anyLong(), anyList(), anyString(),
                eq(authorizationUser));
    }

    @Test
    void getFilteredOverviewShouldReturnExceptionWhenQuarterIdIsNonExistent() {
        when(teamBusinessService.getAllTeams()).thenReturn(List.of(Team.Builder.builder().withId(1L).build()));

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(overviewValidationService)
                .validateOnGet(eq(QUARTER_ID), anyList());

        assertThrows(ResponseStatusException.class,
                () -> overviewBusinessService.getFilteredOverview(QUARTER_ID, null, "", authorizationUser));

        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewValidationService, never()).validateOnGet(QUARTER_ID, teamIds);
        verify(overviewPersistenceService, never()).getFilteredOverview(anyLong(), anyList(), anyString(),
                eq(authorizationUser));
    }

    @Test
    void getFilteredOverviewShouldReturnExceptionWhenTeamIdIsNonExistent() {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(overviewValidationService)
                .validateOnGet(QUARTER_ID, teamIds);

        assertThrows(ResponseStatusException.class,
                () -> overviewBusinessService.getFilteredOverview(QUARTER_ID, teamIds, "", authorizationUser));

        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewValidationService, never()).validateQuarter(QUARTER_ID);
        verify(overviewValidationService, times(1)).validateOnGet(QUARTER_ID, teamIds);
        verify(overviewPersistenceService, never()).getFilteredOverview(anyLong(), anyList(), any(),
                eq(authorizationUser));
    }

    @Test
    void getFilteredOverviewShouldThrowExceptionWhenTeamIdIsNonExistent() {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(overviewValidationService)
                .validateOnGet(QUARTER_ID, teamIds);
        assertThrows(ResponseStatusException.class,
                () -> overviewBusinessService.getFilteredOverview(QUARTER_ID, teamIds, "", authorizationUser));

        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewValidationService, times(1)).validateOnGet(QUARTER_ID, teamIds);
        verify(overviewPersistenceService, never()).getFilteredOverview(anyLong(), anyList(), anyString(),
                eq(authorizationUser));
    }

    @Test
    void getFilteredOverviewShouldReturnSortedListFirstLevelTeamFirst() {
        Long firstLevelTeamId = 9L;
        AuthorizationUser user = mockAuthorizationUser(defaultUser(13L), List.of(firstLevelTeamId, 2L),
                firstLevelTeamId, List.of());
        when(overviewPersistenceService.getFilteredOverview(QUARTER_ID, teamIds, null, user))
                .thenReturn(createOverviews(user));

        List<Overview> overviews = overviewBusinessService.getFilteredOverview(QUARTER_ID, teamIds, null, user);

        assertThat(List.of(OverviewId.of(firstLevelTeamId, 2L, null, null),
                OverviewId.of(firstLevelTeamId, 3L, null, null), OverviewId.of(firstLevelTeamId, 1L, null, null),
                OverviewId.of(firstLevelTeamId, 5L, null, null), OverviewId.of(firstLevelTeamId, 4L, null, null),
                OverviewId.of(2L, 7L, null, null), OverviewId.of(2L, 6L, null, null)))
                        .hasSameElementsAs(getOverviewIds(overviews));
    }

    @Test
    void getFilteredOverviewShouldReturnSortedListFirstLevelTeamsFirst() {
        List<Long> userTeamIds = List.of(1L, 2L);
        List<Long> firstLevelTeamIds = List.of(9L, 5L);
        User user = defaultUser(13L);
        AuthorizationUser authUser = new AuthorizationUser(
                User.Builder.builder().withId(user.getId()).withUsername(user.getUsername())
                        .withFirstname(user.getFirstname()).withLastname(user.getLastname()).withEmail(user.getEmail())
                        .build(),
                userTeamIds, firstLevelTeamIds, List.of(READ_ALL_PUBLISHED, READ_ALL_DRAFT, WRITE_ALL));

        when(overviewPersistenceService.getFilteredOverview(QUARTER_ID, teamIds, null, authUser))
                .thenReturn(createOverviews(authUser, List.of(4L, 3L)));

        List<Overview> overviews = overviewBusinessService.getFilteredOverview(QUARTER_ID, teamIds, null, authUser);

        assertThat(List.of(OverviewId.of(1L, 8L, null, null), OverviewId.of(1L, 7L, null, null),
                OverviewId.of(2L, 10L, null, null), OverviewId.of(2L, 9L, null, null),
                OverviewId.of(5L, 5L, null, null), OverviewId.of(5L, 6L, null, null), OverviewId.of(5L, 4L, null, null),
                OverviewId.of(9L, 2L, null, null), OverviewId.of(9L, 3L, null, null), OverviewId.of(9L, 1L, null, null),
                OverviewId.of(3L, 51L, 153L, null), OverviewId.of(3L, 51L, 154L, null),
                OverviewId.of(4L, 51L, 151L, null), OverviewId.of(4L, 51L, 152L, null)))
                        .hasSameElementsAs(getOverviewIds(overviews));
    }

    @Test
    void getFilteredOverviewShouldReturnSortedListUserTeamsFirst() {
        Long firstLevelTeamId = 5L;
        AuthorizationUser user = mockAuthorizationUser(defaultUser(13L), List.of(9L, 2L), firstLevelTeamId, List.of());
        when(overviewPersistenceService.getFilteredOverview(QUARTER_ID, teamIds, null, user))
                .thenReturn(createOverviews(user));

        List<Overview> overviews = overviewBusinessService.getFilteredOverview(QUARTER_ID, teamIds, null, user);

        assertThat(List.of(OverviewId.of(2L, 7L, null, null), OverviewId.of(2L, 6L, null, null),
                OverviewId.of(9L, 5L, null, null), OverviewId.of(9L, 4L, null, null),
                OverviewId.of(firstLevelTeamId, 2L, null, null), OverviewId.of(firstLevelTeamId, 3L, null, null),
                OverviewId.of(firstLevelTeamId, 1L, null, null))).hasSameElementsAs(getOverviewIds(overviews));
    }

    private List<OverviewId> getOverviewIds(List<Overview> overviews) {
        return overviews.stream().map(Overview::getOverviewId).toList();
    }
}
