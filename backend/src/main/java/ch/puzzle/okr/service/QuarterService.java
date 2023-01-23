package ch.puzzle.okr.service;

import ch.puzzle.okr.dto.StartEndDateDTO;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.repository.QuarterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.*;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class QuarterService {
    protected static final Map<Integer, Integer> yearToBusinessQuarterMap = new HashMap<>(4);

    private final KeyResultService keyResultService;
    static {
        yearToBusinessQuarterMap.put(1, 3);
        yearToBusinessQuarterMap.put(2, 4);
        yearToBusinessQuarterMap.put(3, 1);
        yearToBusinessQuarterMap.put(4, 2);
    }

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
        return String.format("GJ %s/%s-Q%x", shortenYear(firstYear), shortenYear(nextYear), currentQuarter);
    }

    public String padWithZeros(int amount, int number) {
        String format = "%0" + amount + "d";
        return String.format(format, number);
    }

    public StartEndDateDTO getStartAndEndDateOfKeyresult(long keyResultId) {
        KeyResult keyResult = this.keyResultService.getKeyResultById(keyResultId);
        String quarterLabel = keyResult.getObjective().getQuarter().getLabel();

        YearMonth endYearMonth = getYearMonthFromLabel(quarterLabel);
        YearMonth startYearMonth = endYearMonth.minusMonths(2);
        LocalDate startDate = LocalDate.of(startYearMonth.getYear(), startYearMonth.getMonthValue(), 1);
        LocalDate endDate = LocalDate.of(endYearMonth.getYear(), endYearMonth.getMonthValue(),
                endYearMonth.lengthOfMonth());
        return StartEndDateDTO.Builder.builder().withStartDate(startDate).withEndDate(endDate).build();
    }

    private YearMonth getYearMonthFromLabel(String quarterLabel) {
        quarterLabel = quarterLabel.trim().replace("GJ ", "");
        int quarter = Integer.parseInt(quarterLabel.substring(quarterLabel.length() - 1));
        int year = Integer.parseInt(quarterLabel.substring(0, 2));
        int yearQuarter = yearToBusinessQuarterMap.get(quarter);
        int month = yearQuarter * 3;
        return YearMonth.of(year + 2000, month);
    }
}
