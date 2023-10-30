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

import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private final AuthorizationUser authorizationUser = defaultAuthorizationUser();
    private final Overview overview = Overview.Builder.builder()
            .withOverviewId(OverviewId.Builder.builder().withObjectiveId(1L).withTeamId(5L).build())
            .withObjectiveTitle("Objective 1").build();

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
}
