package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.service.persistence.QuarterPersistenceService;
import ch.puzzle.okr.service.validation.QuarterValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.temporal.IsoFields;
import java.util.List;

@Service
public class QuarterBusinessService {
    private static final Logger logger = LoggerFactory.getLogger(QuarterBusinessService.class);

    private final QuarterPersistenceService quarterPersistenceService;
    private final QuarterValidationService validator;

    public QuarterBusinessService(QuarterPersistenceService quarterPersistenceService,
            QuarterValidationService validator) {
        this.quarterPersistenceService = quarterPersistenceService;
        this.validator = validator;
    }

    public Quarter getQuarterById(Long quarterId) {
        validator.validateOnGet(quarterId);
        return quarterPersistenceService.findById(quarterId);
    }

    public List<Quarter> getQuarters() {
        return quarterPersistenceService.getMostCurrentQuarters();
    }

    public Quarter getCurrentQuarter() {
        return quarterPersistenceService.getCurrentQuarter();
    }

    private String shortenYear(int fullYear) {
        return padWithZeros(2, fullYear % 100);
    }

    private String padWithZeros(int amount, int number) {
        String format = "%0" + amount + "d";
        return String.format(format, number);
    }

    private String createQuarterLabel(YearMonth yearMonth) {
        // generation of quarter is two quarter in advance therefore the current quarter of the year corresponds with
        // the business quarter we want to generate
        return String.format("GJ %s/%s-Q%x", shortenYear(yearMonth.getYear()), shortenYear(yearMonth.getYear() + 1),
                yearMonth.get(IsoFields.QUARTER_OF_YEAR));
    }

    private void generateQuarter(YearMonth yearMonth) {
        Quarter quarter = Quarter.Builder.builder().withLabel(createQuarterLabel(yearMonth))
                .withStartDate(yearMonth.plusMonths(4).atDay(1)).withEndDate(yearMonth.plusMonths(6).atEndOfMonth())
                .build();
        quarterPersistenceService.save(quarter);
    }

    public YearMonth getCurrentYearMonth() {
        return YearMonth.now();
    }

    @Scheduled(cron = "0 59 23 L * ?") // Cron expression for 23:59:00 on the last day of every month
    public void scheduledGenerationQuarters() {
        YearMonth yearMonth = getCurrentYearMonth();
        if (yearMonth.getMonthValue() % 3 == 0) {
            logger.info("Generated quarters on last day of month");
            generateQuarter(yearMonth);
        }
    }
}
