package ch.puzzle.okr.service.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static ch.puzzle.okr.OverviewTestHelper.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OverviewValidationServiceTest {
    @Mock
    TeamValidationService teamValidationService;

    @Mock
    QuarterValidationService quarterValidationService;

    @InjectMocks
    OverviewValidationService validator;

    @Test
    void validateOnGet_ShouldCallQuarterValidator() {
        validator.validateQuarter(QUARTER_ID);
        verify(quarterValidationService, times(1)).validateOnGet(QUARTER_ID);
        verify(quarterValidationService, times(1)).doesEntityExist(QUARTER_ID);
    }

    @Test
    void validateOnGet_ShouldCallTeamValidator() {
        validator.validateTeam(TEAM_ID);
        verify(teamValidationService, times(1)).validateOnGet(TEAM_ID);
        verify(teamValidationService, times(1)).doesEntityExist(TEAM_ID);
    }

    @Test
    void validateOnGet_ShouldCallQuarterValidatorAndTeamValidator() {
        validator.validateOnGet(QUARTER_ID, teamIds);
        verify(quarterValidationService, times(1)).validateOnGet(QUARTER_ID);
        verify(quarterValidationService, times(1)).doesEntityExist(QUARTER_ID);
        verify(teamValidationService, times(teamIds.size())).validateOnGet(anyLong());
        verify(teamValidationService, times(teamIds.size())).doesEntityExist(anyLong());
    }
}
