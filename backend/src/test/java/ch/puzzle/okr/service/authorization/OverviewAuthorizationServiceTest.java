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
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
import static ch.puzzle.okr.TestHelper.defaultJwtToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OverviewAuthorizationServiceTest {
    @Mock
    OverviewBusinessService overviewBusinessService;
    @Mock
    AuthorizationService authorizationService;
    @InjectMocks
    private OverviewAuthorizationService overviewAuthorizationService;
    private final Jwt token = defaultJwtToken();
    private final AuthorizationUser authorizationUser = defaultAuthorizationUser();
    private final Overview overview = Overview.Builder.builder()
            .withOverviewId(OverviewId.Builder.builder().withObjectiveId(1L).withTeamId(5L).build())
            .withObjectiveTitle("Objective 1").build();

    @Test
    void getOverviewByQuarterIdAndTeamIds_ShouldReturnOverviews_WhenAuthorized() {
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        when(overviewBusinessService.getOverviewByQuarterIdAndTeamIds(any(), any(), any()))
                .thenReturn(List.of(overview));

        List<Overview> overviews = overviewAuthorizationService.getOverviewByQuarterIdAndTeamIds(1L, List.of(5L),
                token);

        assertThat(List.of(overview)).hasSameElementsAs(overviews);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void getOverviewByQuarterIdAndTeamIds_ShouldSetWritableProperly(boolean isWritable) {
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        when(authorizationService.isWriteable(authorizationUser, 5L)).thenReturn(isWritable);
        when(overviewBusinessService.getOverviewByQuarterIdAndTeamIds(any(), any(), any()))
                .thenReturn(List.of(overview));

        List<Overview> overviews = overviewAuthorizationService.getOverviewByQuarterIdAndTeamIds(1L, List.of(5L),
                token);

        assertEquals(isWritable, overviews.get(0).isWriteable());
    }

    @Test
    void getOverviewByQuarterIdAndTeamIds_ShouldReturnEmptyList_WhenNotAuthorized() {
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        when(overviewBusinessService.getOverviewByQuarterIdAndTeamIds(any(), any(), any())).thenReturn(List.of());

        List<Overview> overviews = overviewAuthorizationService.getOverviewByQuarterIdAndTeamIds(1L, List.of(5L),
                token);
        assertThat(List.of()).hasSameElementsAs(overviews);
    }
}
