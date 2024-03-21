package ch.puzzle.okr.service.business;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.service.persistence.QuarterPersistenceService;
import ch.puzzle.okr.service.validation.QuarterValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuarterBusinessService {
    private static final Logger logger = LoggerFactory.getLogger(QuarterBusinessService.class);

    private final QuarterPersistenceService quarterPersistenceService;
    private final QuarterValidationService validator;

    @Value("${okr.quarter.business.year.start}")
    private int quarterStart;

    @Value("${okr.quarter.label.format}")
    private String quarterFormat;

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
        List<Quarter> mostCurrentQuarterList = quarterPersistenceService.getMostCurrentQuarters();
        Quarter backlog = quarterPersistenceService.findByLabel("Backlog");
        mostCurrentQuarterList.add(0, backlog);
        return mostCurrentQuarterList;
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

    private String createQuarterLabel(YearMonth startOfQuarter, int quarter) {
        // Get Start of current business year
        // Subtract months based on the quarter we are in
        // i.e. quarter=2, subtract (2 - 1) * 3 = 3 Months to get start
        int startOfBusinessYear = startOfQuarter.minusMonths((quarter - 1) * 3L).getYear();

        String yearStart = shortenYear(startOfBusinessYear);
        String yearEnd = shortenYear(startOfBusinessYear + 1);

        return quarterFormat.replace("xx", yearStart).replace("yy", yearEnd).replace("zz", String.valueOf(quarter));
    }

    private void generateQuarter(LocalDateTime start, String label) {
        YearMonth yearMonth = YearMonth.from(start);
        Quarter quarter = Quarter.Builder.builder().withLabel(label).withStartDate(start.toLocalDate())
                .withEndDate(yearMonth.plusMonths(2).atEndOfMonth()).build();
        validator.validateOnGeneration(quarter);
        quarterPersistenceService.save(quarter);
    }

    public YearMonth getCurrentYearMonth() {
        return YearMonth.now();
    }

    public Map<Integer, Integer> generateQuarters() {
        Map<Integer, Integer> quarters = new HashMap<>();
        int quarterIndex = quarterStart - 1;
        for (int i = 0; i < 12; i++) {
            // Get the index of the current month based on quarterStart
            int monthIndex = (i + quarterIndex) % 12;
            // Get the quarter (three months), +1 since we don't want to start with 0
            int quarter = (i / 3) + 1;
            // Return the real month
            quarters.put(monthIndex + 1, quarter);
        }

        return quarters;
    }

    @Scheduled(cron = "0 59 23 L * ?") // Cron expression for 23:59:00 on the last day of every month
    public void scheduledGenerationQuarters() {
        Map<Integer, Integer> quarters = generateQuarters();
        YearMonth currentYearMonth = getCurrentYearMonth();
        YearMonth yearMonthToGenerate = currentYearMonth.plusMonths(4);

        int currentQuarter = quarters.get(currentYearMonth.getMonthValue());
        int nextQuarter = quarters.get(yearMonthToGenerate.getMonthValue());

        // If we are in the last month of a quarter, generate the next quarter
        if (Math.abs(nextQuarter - currentQuarter) > 1) {
            String label = createQuarterLabel(yearMonthToGenerate, nextQuarter);
            generateQuarter(yearMonthToGenerate.atDay(1).atStartOfDay(), label);
        }
    }
}
