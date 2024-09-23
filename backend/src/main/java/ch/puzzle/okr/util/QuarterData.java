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
        logger.warn("currentQuarterLabel    : " + label);
        return label;
    }

    public static String currentQuarterStartDate() {
        String start = QuarterFunction.currentQuarterStartDate();
        logger.warn("currentQuarterStartDate: " + start);
        return start;
    }

    public static String currentQuarterEndDate() {
        String end = QuarterFunction.currentQuarterEndDate();
        logger.warn("currentQuarterEndDate  : " + end);
        return end;
    }

    public static String nextQuarterLabel() {
        String label = QuarterFunction.nextQuarterLabel();
        logger.warn("nextQuarterLabel       : " + label);
        return label;
    }

    public static String nextQuarterStartDate() {
        String start = QuarterFunction.nextQuarterStartDate();
        logger.warn("nextQuarterStartDate   : " + start);
        return start;
    }

    public static String nextQuarterEndDate() {
        String end = QuarterFunction.nextQuarterEndDate();
        logger.warn("nextQuarterEndDate     : " + end);
        return end;
    }
}
