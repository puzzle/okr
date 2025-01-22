package ch.puzzle.okr.service.business;

import static ch.puzzle.okr.Constants.BACK_LOG_QUARTER_LABEL;

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
        Quarter backlog = quarterPersistenceService.findByLabel(BACK_LOG_QUARTER_LABEL);
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

    private boolean isInLastMonthOfQuarter(int currentQuarter, int nextQuarter) {
        // If the quarter 4 months in the future and the current are exactly 2 apart,
        // we are in the final month of the current quarter. This works for all 4 cases:
        // 1 -> 3 | 2 -> 4 | 3 -> 1 | 4 -> 2
        return Math.abs(nextQuarter - currentQuarter) == 2;
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

    @Scheduled(cron = "0 59 23 L * ?") // Cron expression for 23:59:00 on the last day of every month
    public void scheduledGenerationQuarters() {
        Map<Integer, Integer> quarters = generateQuarters();
        YearMonth currentYearMonth = getCurrentYearMonth();
        YearMonth nextQuarterYearMonth = currentYearMonth.plusMonths(4);

        int currentQuarter = quarters.get(currentYearMonth.getMonthValue());
        int nextQuarter = quarters.get(nextQuarterYearMonth.getMonthValue());

        String initialTenant = TenantContext.getCurrentTenant();

        Set<String> tenantSchemas = this.tenantConfigProvider.getAllTenantIds();
        // If we are in the last month of a quarter, generate the next quarter
        if (isInLastMonthOfQuarter(currentQuarter, nextQuarter)) {
            for (String schema : tenantSchemas) {
                logger.info("Generated quarters on last day of month for tenant {}", schema);
                String label = createQuarterLabel(nextQuarterYearMonth, nextQuarter);
                generateQuarter(nextQuarterYearMonth.atDay(1), label, schema);
            }
        }

        TenantContext.setCurrentTenant(initialTenant);
    }
}
