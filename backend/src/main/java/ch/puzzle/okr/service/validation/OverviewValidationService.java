package ch.puzzle.okr.service.validation;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class OverviewValidationService {
    private final TeamValidationService teamValidator;
    private final QuarterValidationService quarterValidator;

    public OverviewValidationService(TeamValidationService teamValidator, QuarterValidationService quarterValidator) {
        this.teamValidator = teamValidator;
        this.quarterValidator = quarterValidator;
    }

    public void validateTeam(Long id) {
        teamValidator.validateOnGet(id);
        teamValidator.doesEntityExist(id);
    }

    public void validateQuarter(Long id) {
        quarterValidator.validateOnGet(id);
        quarterValidator.doesEntityExist(id);
    }

    public void validateOnGet(Long quarterId, List<Long> teamIds) {
        validateQuarter(quarterId);
        teamIds.forEach(this::validateTeam);
    }
}
