package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.models.overview.OverviewId;
import ch.puzzle.okr.service.persistence.OverviewPersistenceService;
import ch.puzzle.okr.service.validation.OverviewValidationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static ch.puzzle.okr.OverviewTestHelper.QUARTER_ID;
import static ch.puzzle.okr.OverviewTestHelper.teamIds;
import static ch.puzzle.okr.TestHelper.*;
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

    private static final LocalDateTime jan = LocalDateTime.of(2024, 1, 1, 0, 0);
    private static final LocalDateTime july = LocalDateTime.of(2024, 7, 7, 0, 0);

    private static List<Overview> createOverviews() {
        return createOverviews(authorizationUser);
    }

    private static List<Overview> createOverviews(AuthorizationUser user) {
        long index = 1L;
        List<Overview> overviews = new ArrayList<>();
        for (Long teamId : user.firstLevelTeamIds()) {
            overviews.addAll((List.of( //
                    Overview.Builder.builder() //
                            .withOverviewId(OverviewId.Builder.builder() //
                                    .withObjectiveId(index++) //
                                    .withTeamId(teamId).build()) //
                            .withObjectiveTitle("ZZZ Objective") //
                            .withTeamName("firstLevelTeam-" + teamId) //
                            .withObjectiveCreatedOn(LocalDateTime.of(2023, 12, 10, 18, 33)) //
                            .build(),
                    Overview.Builder.builder() //
                            .withOverviewId(OverviewId.Builder.builder() //
                                    .withObjectiveId(index++) //
                                    .withTeamId(teamId) //
                                    .build()) //
                            .withObjectiveTitle("AAA Objective") //
                            .withTeamName("firstLevelTeam-" + teamId) //
                            .withObjectiveCreatedOn(LocalDateTime.of(2023, 9, 10, 18, 33)) //
                            .build(),
                    Overview.Builder.builder() //
                            .withOverviewId(OverviewId.Builder.builder() //
                                    .withObjectiveId(index++) //
                                    .withTeamId(teamId).build()) //
                            .withObjectiveTitle("AAA Objective") //
                            .withTeamName("firstLevelTeam-" + teamId) //
                            .withObjectiveCreatedOn(LocalDateTime.of(2023, 9, 10, 18, 33)) //
                            .build())));
        }
        for (Long teamId : user.userTeamIds()) {
            overviews.addAll(List.of( //
                    Overview.Builder.builder() //
                            .withOverviewId(OverviewId.Builder.builder() //
                                    .withObjectiveId(index++) //
                                    .withTeamId(teamId).build()) //
                            .withObjectiveTitle("CCC Objective") //
                            .withTeamName("team-" + teamId) //
                            .withObjectiveCreatedOn(LocalDateTime.of(2023, 10, 21, 18, 33)) //
                            .build(),
                    Overview.Builder.builder() //
                            .withOverviewId(OverviewId.Builder.builder() //
                                    .withObjectiveId(index++) //
                                    .withTeamId(teamId).build()) //
                            .withObjectiveTitle("BBB Objective") //
                            .withTeamName("team-" + teamId) //
                            .withObjectiveCreatedOn(LocalDateTime.of(2023, 10, 1, 8, 53)) //
                            .build()));
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
    void getFilteredOverviewShouldReturnEmptyListOfOverviewsWhenTeamIdsAreNull() {
        List<Overview> overviews = overviewBusinessService.getFilteredOverview(QUARTER_ID, null, "", authorizationUser);

        assertEquals(0, overviews.size());
        verify(overviewValidationService, times(1)).validateOnGet(QUARTER_ID, List.of());
        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewPersistenceService, never()).getFilteredOverview(QUARTER_ID, List.of(), "", authorizationUser);
        verify(overviewPersistenceService, never()).getFilteredOverview(anyLong(), anyList(), anyString(),
                eq(authorizationUser));
    }

    @Test
    void getFilteredOverviewShouldReturnExceptionWhenQuarterIdIsNonExistent() {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(overviewValidationService)
                .validateOnGet(eq(QUARTER_ID), anyList());

        assertThrows(ResponseStatusException.class,
                () -> overviewBusinessService.getFilteredOverview(QUARTER_ID, List.of(), "", authorizationUser));

        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewValidationService, never()).validateOnGet(QUARTER_ID, teamIds);
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

    private List<OverviewId> getOverviewIds(List<Overview> overviews) {
        return overviews.stream().map(Overview::getOverviewId).toList();
    }

    private static Stream<Arguments> generateTestOverviews() {
        return Stream.of(
                // matching userTeams and firstLevelTeams
                Arguments.of(of("team 1", jan), of("team 1", jan), false, false, true, true, 0),
                Arguments.of(of("team 1", jan), of("team 1", july), false, false, true, true, -6),
                Arguments.of(of("team 1", jan), of("team 2", july), false, false, true, true, -1),

                // matching userTeams but non-matching firstLevelTeams
                Arguments.of(of("team 1", jan), of("team 2", july), false, false, true, false, -1),
                Arguments.of(of("team 1", jan), of("team 2", july), false, false, false, true, 1),

                // non-matching userTeams
                Arguments.of(of("team 1", jan), of("team 2", july), true, false, false, false, -1),
                Arguments.of(of("team 1", jan), of("team 2", july), false, true, true, true, 1));
    }

    private static Overview of(String teamName, LocalDateTime createdOn) {
        return Overview.Builder.builder() //
                .withOverviewId(OverviewId.Builder.builder() //
                        .withTeamId((long) 1) //
                        .withObjectiveId((long) 1) //
                        .withKeyResultId((long) 1) //
                        .withCheckInId((long) 1).build())
                .withTeamName(teamName) //
                .withObjectiveCreatedOn(createdOn).build();
    }

    @DisplayName("compare() should sort Overviews")
    @ParameterizedTest
    @MethodSource("generateTestOverviews")
    void compareShouldSortOverviews(Overview o1, Overview o2, //
            boolean containsUserTeam1, boolean containsUserTeam2, boolean containsFirstLevelTeam1,
            boolean containsFirstLevelTeam2, //
            int expectedResult) {
        // arrange
        OverviewBusinessService.OverviewComparator overviewComparator = new OverviewBusinessService.OverviewComparator(
                null);

        // act
        int compareResult = overviewComparator.compare(o1, o2, containsUserTeam1, containsUserTeam2,
                containsFirstLevelTeam1, containsFirstLevelTeam2);

        // assert
        assertEquals(expectedResult, compareResult);
    }

}
