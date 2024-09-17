package ch.puzzle.okr.util.quarter.generate.h2;

import ch.puzzle.okr.service.CacheService;
import ch.puzzle.okr.util.quarter.generate.QuarterData;
import ch.puzzle.okr.util.quarter.generate.Quarters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Quarter functions for H2 database.</br>
 * </br>
 * This class is used for testing purposes only. Do NOT use this class in production mode.
 */
@Component
@Profile("integration-test")
public class QuarterFunction {
    public static final int CURRENT_QUARTER_DB_ID = 2;
    public static final int NEXT_QUARTER_DB_ID = 3;

    private static final Map<Integer, QuarterData> QUARTERS = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(QuarterFunction.class);

    public static void initQuarterData() {
        LocalDate now = LocalDate.now();
        registerCurrentQuarterForDate(now, CURRENT_QUARTER_DB_ID);

        LocalDate in3Months = now.plusMonths(3);
        registerCurrentQuarterForDate(in3Months, NEXT_QUARTER_DB_ID);
    }

    private static void registerCurrentQuarterForDate(LocalDate date, int dbId) {
        Quarters quarters = new Quarters(date.getYear());
        QuarterData currentQuarter = quarters.currentQuarter(date);
        QUARTERS.put(dbId, currentQuarter);
    }

    public static String currentQuarterLabel() {
        String label = QUARTERS.get(CURRENT_QUARTER_DB_ID).label();
        logger.warn("currentQuarterLabel    : " + label);
        return label;
    }

    public static String currentQuarterStartDate() {
        String start = QUARTERS.get(CURRENT_QUARTER_DB_ID).startDateAsIsoString();
        logger.warn("currentQuarterStartDate: " +start);
        return start;
    }

    public static String currentQuarterEndDate() {
        String end = QUARTERS.get(CURRENT_QUARTER_DB_ID).endDateAsIsoString();
        logger.warn("currentQuarterEndDate  : " + end);
        return end;
    }

    public static String nextQuarterLabel() {
        String label = QUARTERS.get(NEXT_QUARTER_DB_ID).label();
        logger.warn("nextQuarterLabel       : " + label);
        return label;
    }

    public static String nextQuarterStartDate() {
        String start = QUARTERS.get(NEXT_QUARTER_DB_ID).startDateAsIsoString();
        logger.warn("nextQuarterStartDate   : " + start);
        return start;
    }

    public static String nextQuarterEndDate() {
        String end = QUARTERS.get(NEXT_QUARTER_DB_ID).endDateAsIsoString();
        logger.warn("nextQuarterEndDate     : " + end);
        return end;
    }

}
