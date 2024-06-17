package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.models.overview.OverviewId;
import ch.puzzle.okr.service.business.OverviewBusinessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static ch.puzzle.okr.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OverviewAuthorizationServiceTest {
    @Mock
    OverviewBusinessService overviewBusinessService;
    @Mock
    AuthorizationService authorizationService;
    @InjectMocks
    private OverviewAuthorizationService overviewAuthorizationService;

    private final long adminTeamId = 5L;
    private final long memberTeamId = 6L;

    private final AuthorizationUser authorizationUser = new AuthorizationUser(
            defaultUserWithTeams(1L, List.of(defaultTeam(adminTeamId)), List.of(defaultTeam(memberTeamId))));
    private final AuthorizationUser okrChampionUser = new AuthorizationUser(defaultOkrChampion(2L));
    private final Overview overview = Overview.Builder.builder()
            .withOverviewId(OverviewId.Builder.builder().withObjectiveId(1L).withTeamId(adminTeamId).build())
            .withObjectiveTitle("Objective 1").build();

    @Test
    void getFilteredOverviewShouldReturnOverviewsWhenAuthorized() {
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(overviewBusinessService.getFilteredOverview(any(), any(), any(), eq(authorizationUser)))
                .thenReturn(List.of(overview));

        List<Overview> overviews = overviewAuthorizationService.getFilteredOverview(1L, List.of(adminTeamId), "");

        assertThat(List.of(overview)).hasSameElementsAs(overviews);
    }

    @Test
    void getFilteredOverviewShouldSetWritableProperlyToTrue() {
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(overviewBusinessService.getFilteredOverview(any(), any(), any(), eq(authorizationUser)))
                .thenReturn(List.of(overview));

        List<Overview> overviews = overviewAuthorizationService.getFilteredOverview(1L, List.of(adminTeamId), "");

        assertTrue(overviews.get(0).isWriteable());
    }

    @Test
    void getFilteredOverviewShouldSetWritableProperlyToFalse() {
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(overviewBusinessService.getFilteredOverview(any(), any(), any(), eq(authorizationUser)))
                .thenReturn(List.of(overview));

        List<Overview> overviews = overviewAuthorizationService.getFilteredOverview(1L, List.of(memberTeamId), "");

        assertTrue(overviews.get(0).isWriteable());
    }

    @Test
    void getFilteredOverviewShouldReturnEmptyListWhenNotAuthorized() {
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(overviewBusinessService.getFilteredOverview(1L, List.of(adminTeamId), "", authorizationUser)).thenReturn(List.of());

        List<Overview> overviews = overviewAuthorizationService.getFilteredOverview(1L, List.of(adminTeamId), "");
        assertThat(List.of()).hasSameElementsAs(overviews);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void hasWriteAllAccessShouldReturnHasRoleWriteAll(boolean hasRoleWriteAll) {
        if (hasRoleWriteAll) {
            when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(okrChampionUser);
        } else {
            when(authorizationService.updateOrAddAuthorizationUser())
                    .thenReturn(mockAuthorizationUser(defaultUser(adminTeamId)));
        }

        assertEquals(hasRoleWriteAll, overviewAuthorizationService.hasWriteAllAccess());
    }
}
