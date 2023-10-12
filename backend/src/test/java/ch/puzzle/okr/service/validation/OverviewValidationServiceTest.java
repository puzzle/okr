package ch.puzzle.okr.service.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static ch.puzzle.okr.OverviewTestHelper.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OverviewValidationServiceTest {
    @MockBean
    TeamValidationService teamValidationService = Mockito.mock(TeamValidationService.class);

    @MockBean
    QuarterValidationService quarterValidationService = Mockito.mock(QuarterValidationService.class);

    @InjectMocks
    OverviewValidationService validator;

    @Test
    void validateOnGet_ShouldCallQuarterValidator() {
        validator.validateQuarter(quarterId);
        verify(quarterValidationService, times(1)).validateOnGet(quarterId);
        verify(quarterValidationService, times(1)).doesEntityExist(quarterId);
    }

    @Test
    void validateOnGet_ShouldCallTeamValidator() {
        validator.validateTeam(teamId);
        verify(teamValidationService, times(1)).validateOnGet(teamId);
        verify(teamValidationService, times(1)).doesEntityExist(teamId);
    }

    @Test
    void validateOnGet_ShouldCallQuarterValidatorAndTeamValidator() {
        validator.validateOnGet(quarterId, teamIds);
        verify(quarterValidationService, times(1)).validateOnGet(quarterId);
        verify(quarterValidationService, times(1)).doesEntityExist(quarterId);
        verify(teamValidationService, times(teamIds.size())).validateOnGet(anyLong());
        verify(teamValidationService, times(teamIds.size())).doesEntityExist(anyLong());
    }
}
