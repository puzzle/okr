package ch.puzzle.okr.service.validation;

import static ch.puzzle.okr.test.OverviewTestHelper.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OverviewValidationServiceTest {
    @Mock
    TeamValidationService teamValidationService;

    @Mock
    QuarterValidationService quarterValidationService;

    @InjectMocks
    OverviewValidationService validator;

    @DisplayName("Should be successful on validateQuarter() when quarter id is valid")
    @Test
    void shouldBeSuccessfulOnValidateQuarterWhenQuarterIdIsValid() {
        validator.validateQuarter(QUARTER_ID);
        verify(quarterValidationService, times(1)).validateOnGet(QUARTER_ID);
        verify(quarterValidationService, times(1)).doesEntityExist(QUARTER_ID);
    }

    @DisplayName("Should be successful on validateTeam() when team id is valid")
    @Test
    void shouldBeSuccessfulOnValidateTeamWhenTeamIdIsValid() {
        validator.validateTeam(TEAM_ID);
        verify(teamValidationService, times(1)).validateOnGet(TEAM_ID);
        verify(teamValidationService, times(1)).doesEntityExist(TEAM_ID);
    }

    @DisplayName("Should be successful on validateOnGet() when quarter and teams are valid")
    @Test
    void shouldBeSuccessfulOnValidateOnGetWhenQuarterAndTeamsAreValid() {
        validator.validateOnGet(QUARTER_ID, teamIds);
        verify(quarterValidationService, times(1)).validateOnGet(QUARTER_ID);
        verify(quarterValidationService, times(1)).doesEntityExist(QUARTER_ID);
        verify(teamValidationService, times(teamIds.size())).validateOnGet(anyLong());
        verify(teamValidationService, times(teamIds.size())).doesEntityExist(anyLong());
    }
}