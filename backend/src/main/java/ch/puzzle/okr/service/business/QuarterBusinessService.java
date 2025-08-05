package ch.puzzle.okr.service.business;

import static ch.puzzle.okr.Constants.BACKLOG_QUARTER_LABEL;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.multitenancy.TenantConfigProvider;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.service.persistence.QuarterPersistenceService;
import ch.puzzle.okr.service.validation.QuarterValidationService;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class QuarterBusinessService {
    private static final Logger logger = LoggerFactory.getLogger(QuarterBusinessService.class);

    private final QuarterPersistenceService quarterPersistenceService;
    private final QuarterValidationService validator;
    private final TenantConfigProvider tenantConfigProvider;

    @Value("${okr.quarter.business.year.start}")
    private int quarterStart;

    @Value("${okr.quarter.label.format}")
    private String quarterFormat;

    public QuarterBusinessService(QuarterPersistenceService quarterPersistenceService,
                                  QuarterValidationService validator, TenantConfigProvider tenantConfigProvider) {
        this.quarterPersistenceService = quarterPersistenceService;
        this.validator = validator;
        this.tenantConfigProvider = tenantConfigProvider;
    }

    public Quarter getQuarterById(Long quarterId) {
        validator.validateOnGet(quarterId);
        return quarterPersistenceService.findById(quarterId);
    }

    public List<Quarter> getQuarters() {
        List<Quarter> mostCurrentQuarterList = quarterPersistenceService.getMostCurrentQuarters();
        Quarter backlog = quarterPersistenceService.findByLabel(BACKLOG_QUARTER_LABEL);
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
        int yearStart = getStartOfBusinessYear(startOfQuarter, quarter);
        int yearEnd = yearStart + 1;

        return StringUtils
                .replaceEach(quarterFormat,
                             new String[]{ "xxxx", "yyyy", "xx", "yy", "zz" },
                             new String[]{ String.valueOf(yearStart), String.valueOf(yearEnd), shortenYear(yearStart),
                                     shortenYear(yearEnd), String.valueOf(quarter) });
    }

    private int getStartOfBusinessYear(YearMonth startOfQuarter, int quarter) {
        // Subtract the amount of months, based on the quarter we are in
        // i.e. quarter=2, subtract (2 - 1) * 3 = 3 Months to get start
        return startOfQuarter.minusMonths((quarter - 1) * 3L).getYear();
    }

    private void generateQuarter(LocalDate start, String label, String schema) {
        TenantContext.setCurrentTenant(schema);

        YearMonth yearMonth = YearMonth.from(start);
        Quarter quarter = Quarter.Builder
                .builder()
                .withLabel(label)
                .withStartDate(start)
                .withEndDate(yearMonth.plusMonths(2).atEndOfMonth())
                .build();
        validator.validateOnGeneration(quarter);
        quarterPersistenceService.save(quarter);
    }

    private boolean isInFirstMonthOfQuarter(int monthOfYear) {
        // The first months of a quarter are 1, 4, 7, 10.
        // In a modulo-3 calculation, these numbers all have a remainder of 1.
        // (1 % 3 = 1, 4 % 3 = 1, 7 % 3 = 1, 10 % 3 = 1)
        // All other months result in a remainder of 2 or 0.
        return monthOfYear % 3 == 1;
    }

    public YearMonth getCurrentYearMonth() {
        return YearMonth.now();
    }

    Map<Integer, Integer> generateQuarters() {
        Map<Integer, Integer> quarters = new HashMap<>();
        int quarterIndexZeroBased = quarterStart - 1;
        for (int i = 0; i < 12; i++) {
            int monthIndexZeroBased = (i + quarterIndexZeroBased) % 12;
            int quarterZeroBased = i / 3;
            quarters.put(monthIndexZeroBased + 1, quarterZeroBased + 1);
        }
        return quarters;
    }

    @Scheduled(cron = "0 1 0 1 * ?") // Cron expression for 00:01:00 on the first day of every month
    public void scheduledGenerationQuarters() {
        Map<Integer, Integer> quarters = generateQuarters();
        YearMonth currentYearMonth = getCurrentYearMonth().minusMonths(1);

        int currentQuarter = quarters.get(currentYearMonth.getMonthValue());

        String initialTenant = TenantContext.getCurrentTenant();
        System.out.println("initialTenant = " + initialTenant);

        Set<String> tenantSchemas = this.tenantConfigProvider.getAllTenantIds();
        // If we are in the first month of a quarter, generate the next quarter
        System.out.println("tenantSchemas = " + tenantSchemas);
        if (isInFirstMonthOfQuarter(currentQuarter)) {
            for (String schema : tenantSchemas) {
                // Set to the start month of the next Quarter
                YearMonth nextQuarterYearMonth = currentYearMonth.plusMonths(3);
                logger.info("Generated quarters on last day of month for tenant {}", schema);
                String label = createQuarterLabel(nextQuarterYearMonth, quarters.get(nextQuarterYearMonth.getMonthValue()));
                generateQuarter(nextQuarterYearMonth.atDay(1), label, schema);
            }
        }

        TenantContext.setCurrentTenant(initialTenant);
    }
}
