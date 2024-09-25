package ch.puzzle.okr.util;

import ch.puzzle.quarter.generate.h2.QuarterFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuarterData {

    private static final Logger logger = LoggerFactory.getLogger(QuarterData.class);

    public static void initQuarterData() {
        QuarterFunction.initQuarterData();
    }

    public static String currentQuarterLabel() {
        String label = QuarterFunction.currentQuarterLabel();
        logger.info("currentQuarterLabel    : " + label);
        return label;
    }

    public static String currentQuarterStartDate() {
        String start = QuarterFunction.currentQuarterStartDate();
        logger.info("currentQuarterStartDate: " + start);
        return start;
    }

    public static String currentQuarterEndDate() {
        String end = QuarterFunction.currentQuarterEndDate();
        logger.info("currentQuarterEndDate  : " + end);
        return end;
    }

    public static String nextQuarterLabel() {
        String label = QuarterFunction.nextQuarterLabel();
        logger.info("nextQuarterLabel       : " + label);
        return label;
    }

    public static String nextQuarterStartDate() {
        String start = QuarterFunction.nextQuarterStartDate();
        logger.info("nextQuarterStartDate   : " + start);
        return start;
    }

    public static String nextQuarterEndDate() {
        String end = QuarterFunction.nextQuarterEndDate();
        logger.info("nextQuarterEndDate     : " + end);
        return end;
    }
}
