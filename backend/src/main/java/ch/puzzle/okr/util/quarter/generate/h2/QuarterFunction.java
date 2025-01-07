package ch.puzzle.okr.util.quarter.generate.h2;

import ch.puzzle.okr.util.quarter.generate.QuarterData;
import ch.puzzle.okr.util.quarter.generate.Quarters;
import java.time.LocalDate;
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

    private static final Logger logger = LoggerFactory.getLogger(QuarterFunction.class);

    private static QuarterData currentQuarter;
    private static QuarterData nextQuarter;

    // used in V100_0_0__TestData.sql
    public static void initQuarterData() {
        initQuarterData(LocalDate.now());
    }

    public static void initQuarterData(LocalDate now) {
        currentQuarter = createQuarterForDate(now);

        LocalDate in3Months = now.plusMonths(3);
        nextQuarter = createQuarterForDate(in3Months);
    }

    private static QuarterData createQuarterForDate(LocalDate date) {
        Quarters quarters = new Quarters(date.getYear());
        return quarters.currentQuarter(date);
    }

    public static String currentQuarterLabel() {
        var label = currentQuarter.label();
        logger.info("currentQuarterLabel    : {}", label);
        return label;
    }

    public static String currentQuarterStartDate() {
        var start = currentQuarter.startDateAsIsoString();
        logger.info("currentQuarterStartDate: {}", start);
        return start;
    }

    public static String currentQuarterEndDate() {
        var end = currentQuarter.endDateAsIsoString();
        logger.info("currentQuarterEndDate  : {}", end);
        return end;
    }

    public static String nextQuarterLabel() {
        var label = nextQuarter.label();
        logger.info("nextQuarterLabel       : {}", label);
        return label;
    }

    public static String nextQuarterStartDate() {
        var start = nextQuarter.startDateAsIsoString();
        logger.info("nextQuarterStartDate   : {}", start);
        return start;
    }

    public static String nextQuarterEndDate() {
        var end = nextQuarter.endDateAsIsoString();
        logger.info("nextQuarterEndDate     : {}", end);
        return end;
    }

}
