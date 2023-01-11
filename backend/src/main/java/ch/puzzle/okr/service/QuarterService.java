package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.repository.QuarterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

@Service
public class QuarterService {

    protected static final Map<Integer, Integer> yearToBusinessQuarterMap = new HashMap<>(4);
    static {
        yearToBusinessQuarterMap.put(1, 3);
        yearToBusinessQuarterMap.put(2, 4);
        yearToBusinessQuarterMap.put(3, 1);
        yearToBusinessQuarterMap.put(4, 2);
    }

    private final QuarterRepository quarterRepository;
    public Calendar calendar;

    public QuarterService(QuarterRepository quarterRepository, Calendar calendar) {
        this.quarterRepository = quarterRepository;
        this.calendar = calendar;
    }

    public Quarter getQuarterById(Long quarterId) {
        return quarterRepository.findById(quarterId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                (String.format("Quarter with id %d not found", quarterId))));
    }

    public List<Quarter> getOrCreateQuarters() {
        YearMonth now = YearMonth.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);

        String currentQuarterLabel = this.getQuarter(now);
        List<String> futureQuarter = getFutureQuarters(now, 1);
        List<String> pastQuarter = getPastQuarters(now, 4);

        List<String> quarterLabelList = new ArrayList<>();
        quarterLabelList.add(currentQuarterLabel);
        quarterLabelList.addAll(futureQuarter);
        quarterLabelList.addAll(pastQuarter);

        return quarterLabelList.stream().map(this::getOrCreateQuarter).toList();
    }

    protected Quarter getOrCreateQuarter(String label) {
        Optional<Quarter> quarter = quarterRepository.findByLabel(label);
        return quarter.orElseGet(() -> quarterRepository.save(Quarter.Builder.builder().withLabel(label).build()));
    }

    public List<String> getPastQuarters(YearMonth yearMonth, int amount) {
        return IntStream.range(1, amount + 1).map(quarter -> quarter * 3).mapToObj(quarter -> getQuarter(yearMonth.minusMonths(quarter)))
                .toList();
    }

    public List<String> getFutureQuarters(YearMonth yearMonth, int amount) {
        return IntStream.range(1, amount + 1).map(quarter -> quarter * 3).mapToObj(quarter -> getQuarter(yearMonth.plusMonths(quarter)))
                .toList();
    }

    public String getQuarter(YearMonth yearMonth) {
        int currentYear = yearMonth.getYear();
        int currentMonth = yearMonth.getMonthValue();
        int yearQuarter = currentMonth / 3 + 1;
        int firstLabelYear = currentMonth < Calendar.JULY ? currentYear - 1 : currentYear;

        int businessQuarter = yearToBusinessQuarterMap.get(yearQuarter);

        return generateQuarterLabel(firstLabelYear, businessQuarter);
    }

    public String shortenYear(int fullYear) {
        return padWithZeros(2, fullYear % 100);
    }

    public String generateQuarterLabel(int firstYear, int currentQuarter) {
        int nextYear = firstYear + 1;
        return "GJ " + shortenYear(firstYear) + "/" + shortenYear(nextYear) + "-Q" + currentQuarter;
    }

    public <T> List<T> toList(final Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false).toList();
    }

    public String padWithZeros(int amount, int number) {
        return String.format("%0" + amount + "d", number);
    }
}
