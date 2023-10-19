package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.models.overview.OverviewId;
import ch.puzzle.okr.service.business.OverviewBusinessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
import static ch.puzzle.okr.TestHelper.defaultJwtToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OverviewAuthenticationServiceTest {
    @Mock
    OverviewBusinessService overviewBusinessService;
    @Mock
    AuthorizationService authorizationService;
    @InjectMocks
    private OverviewAuthenticationService overviewAuthenticationService;
    private final Jwt token = defaultJwtToken();
    private final AuthorizationUser authorizationUser = defaultAuthorizationUser();
    private final Overview overiew = Overview.Builder.builder()
            .withOverviewId(OverviewId.Builder.builder().withObjectiveId(1L).build()).withObjectiveTitle("Objective 1")
            .build();

    @Test
    void getOverviewByQuarterIdAndTeamIds_ShouldReturnOverviews_WhenAuthorized() {
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        when(overviewBusinessService.getOverviewByQuarterIdAndTeamIds(any(), any(), any()))
                .thenReturn(List.of(overiew));

        List<Overview> overviews = overviewAuthenticationService.getOverviewByQuarterIdAndTeamIds(1L, List.of(5L),
                token);
        assertThat(List.of(overiew)).hasSameElementsAs(overviews);
    }

    @Test
    void getOverviewByQuarterIdAndTeamIds_ShouldReturnEmptyList_WhenNotAuthorized() {
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        when(overviewBusinessService.getOverviewByQuarterIdAndTeamIds(any(), any(), any())).thenReturn(List.of());

        List<Overview> overviews = overviewAuthenticationService.getOverviewByQuarterIdAndTeamIds(1L, List.of(5L),
                token);
        assertThat(List.of()).hasSameElementsAs(overviews);
    }
}
