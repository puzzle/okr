package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.service.persistence.OverviewPersistenceService;
import ch.puzzle.okr.service.validation.OverviewValidationService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
public class OverviewBusinessService {
    private final OverviewPersistenceService overviewPersistenceService;
    private final QuarterBusinessService quarterBusinessService;
    private final OverviewValidationService validator;

    public OverviewBusinessService(OverviewPersistenceService overviewPersistenceService, QuarterBusinessService quarterBusinessService, OverviewValidationService validator) {
        this.overviewPersistenceService = overviewPersistenceService;
        this.quarterBusinessService = quarterBusinessService;
        this.validator = validator;
    }

    public List<Overview> getOverviewByQuarterIdAndTeamIds(Long quarterId, List<Long> teamIds) {
        if (Objects.isNull(quarterId)) {
            quarterId = quarterBusinessService.getCurrentQuarter().getId();
        }
        if (CollectionUtils.isEmpty(teamIds)) {
            // TODO get current team (of current user) if teamIds is empty and remove temp implementation
            // TODO remove line below as soon as teamids are able to be read from jwt token
            validator.validateQuarter(quarterId);
            return overviewPersistenceService.getOverviewByQuarterId(quarterId);
        }
        validator.validateOnGet(quarterId, teamIds);
        return overviewPersistenceService.getOverviewByQuarterIdAndTeamIds(quarterId, teamIds);
    }
}
