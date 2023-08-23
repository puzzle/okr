package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.service.persistance.QuarterPersistenceService;
import ch.puzzle.okr.service.validation.QuarterValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.YearMonth;
import java.time.temporal.IsoFields;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class QuarterService {
    protected static final Map<Integer, Integer> yearToBusinessQuarterMap = new HashMap<>(4);
    private static final Logger logger = LoggerFactory.getLogger(QuarterService.class);

    static {
        yearToBusinessQuarterMap.put(1, 3);
        yearToBusinessQuarterMap.put(2, 4);
        yearToBusinessQuarterMap.put(3, 1);
        yearToBusinessQuarterMap.put(4, 2);
    }

    private final KeyResultService keyResultService;
    private final QuarterPersistenceService quarterPersistenceService;
    private final QuarterValidationService validator;
    public YearMonth now;

    public QuarterService(KeyResultService keyResultService, QuarterPersistenceService quarterPersistenceService, QuarterValidationService validator,
                          YearMonth now) {
        this.keyResultService = keyResultService;
        this.quarterPersistenceService = quarterPersistenceService;
        this.validator = validator;
        this.now = now;
    }

    public Quarter getQuarterById(Long quarterId) {
        validator.validateOnGet(quarterId);
        return quarterPersistenceService.findById(quarterId);
    }

    public List<Quarter> getOrCreateQuarters() {

        List<String> futureQuarter = getFutureQuarters(now, 1);
        List<String> pastQuarter = getPastQuarters(now, 4);

        List<String> quarterLabelList = new ArrayList<>();
//        quarterLabelList.add(currentQuarterLabel);
        quarterLabelList.addAll(futureQuarter);
        quarterLabelList.addAll(pastQuarter);

        return quarterLabelList.stream().map(this::getOrCreateQuarter).toList();
    }

    protected synchronized Quarter getOrCreateQuarter(String label) {
        Optional<Quarter> quarter = quarterPersistenceService.getByLabel(label);
        return quarter
                .orElseGet(() -> quarterPersistenceService.save(Quarter.Builder.builder().withLabel(label).build()));
    }

    public Quarter getActiveQuarter() {
        return getOrCreateQuarter(createQuarterLabel(this.now));
    }

    public List<String> getPastQuarters(YearMonth yearMonth, int amount) {
        return IntStream.range(1, amount + 1).map(quarter -> quarter * 3)
                .mapToObj(quarter -> createQuarterLabel(yearMonth.minusMonths(quarter))).toList();
    }

    public List<String> getFutureQuarters(YearMonth yearMonth, int amount) {
        return IntStream.range(1, amount + 1).map(quarter -> quarter * 3)
                .mapToObj(quarter -> createQuarterLabel(yearMonth.plusMonths(quarter))).toList();
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
        int activeBusinessYear = yearMonth.isBefore(YearMonth.of(yearMonth.getYear(),Month.JULY)) ? yearMonth.getYear() - 1 : yearMonth.getYear();
        return String.format("GJ %s/%s-Q%x", shortenYear(activeBusinessYear), shortenYear(activeBusinessYear+1), businessQuarter);
    }

    private void generateQuarter(YearMonth yearMonth) {
        // Logic to generate quarter
        Quarter quarter = Quarter.Builder.builder()
                .withLabel(createQuarterLabel(yearMonth))
                .withStartDate(yearMonth.plusMonths(4).atDay(1))
                .withEndDate(yearMonth.plusMonths(6).atEndOfMonth())
                .build();
        quarterPersistenceService.save(quarter);
    }

    @Scheduled(cron = "0 59 23 L * ?") // Cron expression for 23:59:00 on the last day of every month
    public void scheduledGenerationQuarters() {
        if (YearMonth.now().getMonthValue() % 3 == 0) {
            generateQuarter(YearMonth.now());
            logger.info("Generated quarters on first day of month");
        }
    }
}
