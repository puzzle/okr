package ch.puzzle.okr.util.quarter.generate.h2;

import ch.puzzle.okr.util.quarter.generate.QuarterData;
import ch.puzzle.okr.util.quarter.generate.Quarters;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Quarter functions for H2 database.</br>
 * </br>
 * This class is used for testing purposes only. Do NOT use this class in
 * production mode.
 */
@Component
@Profile("integration-test")
public class QuarterFunction {
    public static final int CURRENT_QUARTER_DB_ID = 2;
    public static final int NEXT_QUARTER_DB_ID = 3;

    private static final Map<Integer, QuarterData> quarters = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(QuarterFunction.class);

    public static void initQuarterData() {
        LocalDate now = LocalDate.now();
        registerCurrentQuarterForDate(now, CURRENT_QUARTER_DB_ID);

        LocalDate inThreeMonths = now.plusMonths(3);
        registerCurrentQuarterForDate(inThreeMonths, NEXT_QUARTER_DB_ID);
    }

    private static void registerCurrentQuarterForDate(LocalDate date, int dbId) {
        Quarters quarters = new Quarters(date.getYear());
        QuarterData currentQuarter = quarters.currentQuarter(date);
        QuarterFunction.quarters.put(dbId, currentQuarter);
    }

    public static String currentQuarterLabel() {
        String label = quarters.get(CURRENT_QUARTER_DB_ID).label();
        logger.info("currentQuarterLabel    : {}", label);
        return label;

    }

    public static String currentQuarterStartDate() {
        String start = quarters.get(CURRENT_QUARTER_DB_ID).startDateAsIsoString();
        logger.info("currentQuarterStartDate: {}", start);
        return start;
    }

    public static String currentQuarterEndDate() {
        String end = quarters.get(CURRENT_QUARTER_DB_ID).endDateAsIsoString();
        logger.info("currentQuarterEndDate  : {}", end);
        return end;
    }

    public static String nextQuarterLabel() {
        String label = quarters.get(NEXT_QUARTER_DB_ID).label();
        logger.info("nextQuarterLabel       : {}", label);
        return label;
    }

    public static String nextQuarterStartDate() {
        String start = quarters.get(NEXT_QUARTER_DB_ID).startDateAsIsoString();
        logger.info("nextQuarterStartDate   : {}", start);
        return start;
    }

    public static String nextQuarterEndDate() {
        String end = quarters.get(NEXT_QUARTER_DB_ID).endDateAsIsoString();
        logger.info("nextQuarterEndDate     : {}", end);
        return end;
    }

}
