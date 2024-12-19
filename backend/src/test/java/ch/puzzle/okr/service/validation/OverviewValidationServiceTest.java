package ch.puzzle.okr.service.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static ch.puzzle.okr.test.OverviewTestHelper.*;
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

    @DisplayName("Should call quarter validator on validateQuarter()")
    @Test
    void validateOnGetShouldCallQuarterValidator() {
        validator.validateQuarter(QUARTER_ID);
        verify(quarterValidationService, times(1)).validateOnGet(QUARTER_ID);
        verify(quarterValidationService, times(1)).doesEntityExist(QUARTER_ID);
    }

    @DisplayName("Should call team validator on validateTeam()")
    @Test
    void validateOnGetShouldCallTeamValidator() {
        validator.validateTeam(TEAM_ID);
        verify(teamValidationService, times(1)).validateOnGet(TEAM_ID);
        verify(teamValidationService, times(1)).doesEntityExist(TEAM_ID);
    }

    @DisplayName("Should call team validator and quarter validator on validateOnGet()")
    @Test
    void validateOnGetShouldCallQuarterValidatorAndTeamValidator() {
        validator.validateOnGet(QUARTER_ID, teamIds);
        verify(quarterValidationService, times(1)).validateOnGet(QUARTER_ID);
        verify(quarterValidationService, times(1)).doesEntityExist(QUARTER_ID);
        verify(teamValidationService, times(teamIds.size())).validateOnGet(anyLong());
        verify(teamValidationService, times(teamIds.size())).doesEntityExist(anyLong());
    }
}
