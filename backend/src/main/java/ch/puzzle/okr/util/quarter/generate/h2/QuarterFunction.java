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
    private static final int CURRENT_QUARTER_MAP_KEY = 0;
    private static final int NEXT_QUARTER_MAP_KEY = 1;

    private static final Map<Integer, QuarterData> QUARTERS = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(QuarterFunction.class);

    // used in V100_0_0__TestData.sql
    public static void initQuarterData() {
        initQuarterData(LocalDate.now());
    }

    public static void initQuarterData(LocalDate now) {
        registerCurrentQuarterForDate(now, CURRENT_QUARTER_MAP_KEY);

        LocalDate in3Months = now.plusMonths(3);
        registerCurrentQuarterForDate(in3Months, NEXT_QUARTER_MAP_KEY);
    }

    private static void registerCurrentQuarterForDate(LocalDate date, int mapKey) {
        Quarters quarters = new Quarters(date.getYear());
        QuarterData currentQuarter = quarters.currentQuarter(date);
        QUARTERS.put(mapKey, currentQuarter);
    }

    public static String currentQuarterLabel() {
        var label = QUARTERS.get(CURRENT_QUARTER_MAP_KEY).label();
        logger.info("currentQuarterLabel    : {}", label);
        return label;
    }

    public static String currentQuarterStartDate() {
        var start = QUARTERS.get(CURRENT_QUARTER_MAP_KEY).startDateAsIsoString();
        logger.info("currentQuarterStartDate: {}", start);
        return start;
    }

    public static String currentQuarterEndDate() {
        var end = QUARTERS.get(CURRENT_QUARTER_MAP_KEY).endDateAsIsoString();
        logger.info("currentQuarterEndDate  : {}", end);
        return end;
    }

    public static String nextQuarterLabel() {
        var label = QUARTERS.get(NEXT_QUARTER_MAP_KEY).label();
        logger.info("nextQuarterLabel       : {}", label);
        return label;
    }

    public static String nextQuarterStartDate() {
        var start = QUARTERS.get(NEXT_QUARTER_MAP_KEY).startDateAsIsoString();
        logger.info("nextQuarterStartDate   : {}", start);
        return start;
    }

    public static String nextQuarterEndDate() {
        var end = QUARTERS.get(NEXT_QUARTER_MAP_KEY).endDateAsIsoString();
        logger.info("nextQuarterEndDate     : {}", end);
        return end;
    }

}
