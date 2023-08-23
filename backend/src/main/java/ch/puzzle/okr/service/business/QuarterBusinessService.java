package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.service.persistance.QuarterPersistenceService;
import ch.puzzle.okr.service.validation.QuarterValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
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

        String currentQuarterLabel = createQuarterLabelParameters(now);
        List<String> futureQuarter = getFutureQuarters(now, 1);
        List<String> pastQuarter = getPastQuarters(now, 4);

        List<String> quarterLabelList = new ArrayList<>();
        quarterLabelList.add(currentQuarterLabel);
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
        return getOrCreateQuarter(createQuarterLabelParameters(this.now));
    }

    public List<String> getPastQuarters(YearMonth yearMonth, int amount) {
        return IntStream.range(1, amount + 1).map(quarter -> quarter * 3)
                .mapToObj(quarter -> createQuarterLabelParameters(yearMonth.minusMonths(quarter))).toList();
    }

    public List<String> getFutureQuarters(YearMonth yearMonth, int amount) {
        return IntStream.range(1, amount + 1).map(quarter -> quarter * 3)
                .mapToObj(quarter -> createQuarterLabelParameters(yearMonth.plusMonths(quarter))).toList();
    }

    public String shortenYear(int fullYear) {
        return padWithZeros(2, fullYear % 100);
    }


    public String padWithZeros(int amount, int number) {
        String format = "%0" + amount + "d";
        return String.format(format, number);
    }

    public String createQuarterLabelParameters(YearMonth yearMonth) {
        int currentYear = yearMonth.getYear();
        int currentMonth = yearMonth.getMonthValue();
        int yearQuarter = (currentMonth - 1) / 3 + 1;
        int firstLabelYear = currentMonth < Month.JULY.getValue() ? currentYear - 1 : currentYear;
        int businessQuarter = yearToBusinessQuarterMap.get(yearQuarter);

        return generateQuarterLabel(firstLabelYear, businessQuarter);
    }

    public String generateQuarterLabel(int currentYear, int currentQuarter) {
        int nextYear = currentYear + 1;
        return String.format("GJ %s/%s-Q%x", shortenYear(currentYear), shortenYear(nextYear), currentQuarter);
    }

    private void generateQuarter(LocalDate currentDate, YearMonth yearMonth) {
        // Logic to generate quarter
        Quarter quarter = Quarter.Builder.builder()
                .withLabel(createQuarterLabelParameters(yearMonth))
                .withStartDate(currentDate)
                .withEndDate()
                .build();
        quarterPersistenceService.save(quarter);
    }

    @Scheduled(cron = "0 59 23 L * ?") // Cron expression for 23:59:00 on the last day of every month
    public void scheduledGenerationQuarters() {
        if (YearMonth.now().getMonthValue() % 3 == 0) {
            generateQuarter(LocalDate.now(), YearMonth.now());
            logger.info("Generated quarters on first day of month");
        }
    }
}
