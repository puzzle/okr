package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.models.overview.OverviewId;
import ch.puzzle.okr.service.business.OverviewBusinessService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static ch.puzzle.okr.test.TestHelper.*;
import static ch.puzzle.okr.models.authorization.AuthorizationRole.READ_ALL_PUBLISHED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OverviewAuthorizationServiceTest {
    @Mock
    OverviewBusinessService overviewBusinessService;
    @Mock
    AuthorizationService authorizationService;
    @InjectMocks
    private OverviewAuthorizationService overviewAuthorizationService;
    private final AuthorizationUser authorizationUser = defaultAuthorizationUser();
    private final Overview overview = Overview.Builder.builder()
            .withOverviewId(OverviewId.Builder.builder().withObjectiveId(1L).withTeamId(5L).build())
            .withObjectiveTitle("Objective 1").build();

    @DisplayName("getFilteredOverview() should do nothing when OverviewId is null")
    @Test
    void getFilteredOverviewShouldDoNothingWhenOverviewIdIsNull() {
        // arrange
        Overview overviewWithoutOverviewId = mock(Overview.class);
        when(overviewWithoutOverviewId.getOverviewId()).thenReturn(null);

        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(overviewBusinessService.getFilteredOverview(any(), any(), any(), eq(authorizationUser)))
                .thenReturn(List.of(overviewWithoutOverviewId));

        // act
        overviewAuthorizationService.getFilteredOverview(1L, List.of(5L), "");

        // assert
        verify(overviewWithoutOverviewId, never()).setWriteable(anyBoolean());
    }

    @DisplayName("getFilteredOverview() should do nothing when TeamId is null")
    @Test
    void getFilteredOverviewShouldDoNothingWhenTeamIdIsNull() {
        // arrange
        OverviewId overviewId = mock(OverviewId.class);
        when(overviewId.getTeamId()).thenReturn(null);
        Overview overviewWithoutTeamId = mock(Overview.class);
        when(overviewWithoutTeamId.getOverviewId()).thenReturn(overviewId);

        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(overviewBusinessService.getFilteredOverview(any(), any(), any(), eq(authorizationUser)))
                .thenReturn(List.of(overviewWithoutTeamId));

        // act
        overviewAuthorizationService.getFilteredOverview(1L, List.of(5L), "");

        // assert
        verify(overviewWithoutTeamId, never()).setWriteable(anyBoolean());
    }

    @DisplayName("getFilteredOverview() should do nothing when ObjectiveId is null")
    @Test
    void getFilteredOverviewShouldDoNothingWhenObjectiveIdIsNull() {
        // arrange
        OverviewId overviewId = mock(OverviewId.class);
        when(overviewId.getObjectiveId()).thenReturn(null);
        Overview overviewWithoutObjectiveId = mock(Overview.class);
        when(overviewWithoutObjectiveId.getOverviewId()).thenReturn(overviewId);

        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(overviewBusinessService.getFilteredOverview(any(), any(), any(), eq(authorizationUser)))
                .thenReturn(List.of(overviewWithoutObjectiveId));

        // act
        overviewAuthorizationService.getFilteredOverview(1L, List.of(5L), "");

        // assert
        verify(overviewWithoutObjectiveId, never()).setWriteable(anyBoolean());
    }

    @Test
    void getFilteredOverviewShouldReturnOverviewsWhenAuthorized() {
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(overviewBusinessService.getFilteredOverview(any(), any(), any(), eq(authorizationUser)))
                .thenReturn(List.of(overview));

        List<Overview> overviews = overviewAuthorizationService.getFilteredOverview(1L, List.of(5L), "");

        assertThat(List.of(overview)).hasSameElementsAs(overviews);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void getFilteredOverviewShouldSetWritableProperly(boolean isWritable) {
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(authorizationService.isWriteable(authorizationUser, 5L)).thenReturn(isWritable);
        when(overviewBusinessService.getFilteredOverview(any(), any(), any(), eq(authorizationUser)))
                .thenReturn(List.of(overview));

        List<Overview> overviews = overviewAuthorizationService.getFilteredOverview(1L, List.of(5L), "");

        assertEquals(isWritable, overviews.get(0).isWriteable());
    }

    @Test
    void getFilteredOverviewShouldReturnEmptyListWhenNotAuthorized() {
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(overviewBusinessService.getFilteredOverview(1L, List.of(5L), "", authorizationUser)).thenReturn(List.of());

        List<Overview> overviews = overviewAuthorizationService.getFilteredOverview(1L, List.of(5L), "");
        assertThat(List.of()).hasSameElementsAs(overviews);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void hasWriteAllAccessShouldReturnHasRoleWriteAll(boolean hasRoleWriteAll) {
        if (hasRoleWriteAll) {
            when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        } else {
            when(authorizationService.getAuthorizationUser())
                    .thenReturn(mockAuthorizationUser(defaultUser(5L), List.of(), 7L, List.of(READ_ALL_PUBLISHED)));
        }

        assertEquals(hasRoleWriteAll, overviewAuthorizationService.hasWriteAllAccess());
    }
}
