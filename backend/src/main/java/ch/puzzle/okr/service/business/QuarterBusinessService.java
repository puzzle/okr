package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.repository.QuarterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    private final QuarterRepository quarterRepository;
    public YearMonth now;

    public QuarterService(KeyResultService keyResultService, QuarterRepository quarterRepository, YearMonth now) {
        this.keyResultService = keyResultService;
        this.quarterRepository = quarterRepository;
        this.now = now;
    }

    public Quarter getQuarterById(Long quarterId) {
        return quarterRepository.findById(quarterId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                (String.format("Quarter with id %d not found", quarterId))));
    }

    public List<Quarter> getOrCreateQuarters() {

        String currentQuarterLabel = getQuarter(now);
        List<String> futureQuarter = getFutureQuarters(now, 1);
        List<String> pastQuarter = getPastQuarters(now, 4);

        List<String> quarterLabelList = new ArrayList<>();
        quarterLabelList.add(currentQuarterLabel);
        quarterLabelList.addAll(futureQuarter);
        quarterLabelList.addAll(pastQuarter);

        return quarterLabelList.stream().map(this::getOrCreateQuarter).toList();
    }

    protected synchronized Quarter getOrCreateQuarter(String label) {
        Optional<Quarter> quarter = quarterRepository.findByLabel(label);
        return quarter.orElseGet(() -> quarterRepository.save(Quarter.Builder.builder().withLabel(label).build()));
    }

    public Quarter getActiveQuarter() {
        return getOrCreateQuarter(getQuarter(this.now));
    }

    public List<String> getPastQuarters(YearMonth yearMonth, int amount) {
        return IntStream.range(1, amount + 1).map(quarter -> quarter * 3)
                .mapToObj(quarter -> getQuarter(yearMonth.minusMonths(quarter))).toList();
    }

    public List<String> getFutureQuarters(YearMonth yearMonth, int amount) {
        return IntStream.range(1, amount + 1).map(quarter -> quarter * 3)
                .mapToObj(quarter -> getQuarter(yearMonth.plusMonths(quarter))).toList();
    }

    public String getQuarter(YearMonth yearMonth) {
        int currentYear = yearMonth.getYear();
        int currentMonth = yearMonth.getMonthValue();
        int yearQuarter = (currentMonth - 1) / 3 + 1;
        int firstLabelYear = currentMonth < Month.JULY.getValue() ? currentYear - 1 : currentYear;
        int businessQuarter = yearToBusinessQuarterMap.get(yearQuarter);

        return generateQuarterLabel(firstLabelYear, businessQuarter);
    }

    public String shortenYear(int fullYear) {
        return padWithZeros(2, fullYear % 100);
    }

    public String generateQuarterLabel(int firstYear, int currentQuarter) {
        int nextYear = firstYear + 1;
        return String.format("GJ %s/%s-Q%x", shortenYear(firstYear), shortenYear(nextYear), currentQuarter);
    }

    public String padWithZeros(int amount, int number) {
        String format = "%0" + amount + "d";
        return String.format(format, number);
    }

    @Scheduled(cron = "0 59 23 L * ?") // Cron expression for 23:59:00 on the last day of every month
    public void scheduledGenerationQuarters() {
        getOrCreateQuarters();
        logger.info("Generated quarters on first day of month");
    }
}
