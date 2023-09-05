package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.service.persistence.QuarterPersistenceService;
import ch.puzzle.okr.service.validation.QuarterValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.YearMonth;
import java.time.temporal.IsoFields;
import java.util.*;

@Service
public class QuarterBusinessService {
    public static final Map<Integer, Integer> yearToBusinessQuarterMap = new HashMap<>(4);
    private static final Logger logger = LoggerFactory.getLogger(QuarterBusinessService.class);

    static {
        yearToBusinessQuarterMap.put(1, 3);
        yearToBusinessQuarterMap.put(2, 4);
        yearToBusinessQuarterMap.put(3, 1);
        yearToBusinessQuarterMap.put(4, 2);
    }

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
        List<Quarter> quarterList = quarterPersistenceService.getMostCurrentQuarters();
        Quarter buffer = quarterList.get(0);
        quarterList.set(0, quarterList.get(1));
        quarterList.set(1, buffer);
        quarterList.forEach(quarter -> validator.validateOnGet(quarter.getId()));
        return quarterList;
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
        int businessQuarter = yearToBusinessQuarterMap.get(yearMonth.get(IsoFields.QUARTER_OF_YEAR));
        int activeBusinessYear = yearMonth.isBefore(YearMonth.of(yearMonth.getYear(), Month.JULY))
                ? yearMonth.getYear() - 1 : yearMonth.getYear();
        return String.format("GJ %s/%s-Q%x", shortenYear(activeBusinessYear), shortenYear(activeBusinessYear + 1),
                businessQuarter);
    }

    private Quarter generateQuarter(YearMonth yearMonth) {
        // Logic to generate quarter
        Quarter quarter = Quarter.Builder.builder().withLabel(createQuarterLabel(yearMonth))
                .withStartDate(yearMonth.plusMonths(4).atDay(1)).withEndDate(yearMonth.plusMonths(6).atEndOfMonth())
                .build();
        validator.validateOnSave(quarter);
        return quarterPersistenceService.save(quarter);
    }

    @Scheduled(cron = "0 59 23 L * ?") // Cron expression for 23:59:00 on the last day of every month
    public Quarter scheduledGenerationQuarters() {
        if (YearMonth.now().getMonthValue() % 3 == 0) {
            logger.info("Generated quarters on first day of month");
            return generateQuarter(YearMonth.now());
        }
        return null;
    }
}
