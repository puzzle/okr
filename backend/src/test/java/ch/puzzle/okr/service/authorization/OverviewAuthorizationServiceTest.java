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
import static ch.puzzle.okr.models.authorization.AuthorizationRole.READ_ALL_PUBLISHED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    void getFilteredOverviewShouldReturnOverviewsWhenAuthorized() {
        // arrange
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(overviewBusinessService.getFilteredOverview(any(), any(), any(), eq(authorizationUser)))
                .thenReturn(List.of(overview));
        when(authorizationService.isWriteable(authorizationUser, 5L)).thenReturn(true);

        // act
        List<Overview> overviews = overviewAuthorizationService.getFilteredOverview(1L, List.of(5L), "");

        // assert
        verify(authorizationService, times(1)).isWriteable(authorizationUser, 5L);
        assertThat(List.of(overview)).hasSameElementsAs(overviews);
        assertTrue(overviews.get(0).isWriteable());
    }

    @Test
    void getFilteredOverviewShouldReturnNotWriteableOverviewsWhenArchived() {
        // arrange
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(overviewBusinessService.getFilteredOverview(any(), any(), any(), eq(authorizationUser)))
                .thenReturn(List.of(overview));

        // act
        List<Overview> overviews = overviewAuthorizationService.getFilteredOverview(998L, List.of(5L), "");

        // assert
        verify(authorizationService, times(0)).isWriteable(authorizationUser, 5L);
        assertFalse(overviews.get(0).isWriteable());
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void getFilteredOverviewShouldSetWritableProperly(boolean isWritable) {
        // arrange
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(authorizationService.isWriteable(authorizationUser, 5L)).thenReturn(isWritable);
        when(overviewBusinessService.getFilteredOverview(any(), any(), any(), eq(authorizationUser)))
                .thenReturn(List.of(overview));

        // act
        List<Overview> overviews = overviewAuthorizationService.getFilteredOverview(1L, List.of(5L), "");

        // assert
        assertEquals(isWritable, overviews.get(0).isWriteable());
    }

    @Test
    void getFilteredOverviewShouldReturnEmptyListWhenNotAuthorized() {
        // arrange
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(overviewBusinessService.getFilteredOverview(1L, List.of(5L), "", authorizationUser)).thenReturn(List.of());

        // act
        List<Overview> overviews = overviewAuthorizationService.getFilteredOverview(1L, List.of(5L), "");

        // assert
        assertThat(List.of()).hasSameElementsAs(overviews);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void hasWriteAllAccessShouldReturnHasRoleWriteAll(boolean hasRoleWriteAll) {
        // arrange
        if (hasRoleWriteAll) {
            when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        } else {
            when(authorizationService.getAuthorizationUser())
                    .thenReturn(mockAuthorizationUser(defaultUser(5L), List.of(), 7L, List.of(READ_ALL_PUBLISHED)));
        }

        // assert
        assertEquals(hasRoleWriteAll, overviewAuthorizationService.hasWriteAllAccess());
    }
}
