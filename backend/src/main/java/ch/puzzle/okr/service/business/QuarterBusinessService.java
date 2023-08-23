package ch.puzzle.okr.service.business;

import ch.puzzle.okr.dto.StartEndDateDTO;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.service.persistence.QuarterPersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@Service
public class QuarterBusinessService {
    protected static final Map<Integer, Integer> yearToBusinessQuarterMap = new HashMap<>(4);

    static {
        yearToBusinessQuarterMap.put(1, 3);
        yearToBusinessQuarterMap.put(2, 4);
        yearToBusinessQuarterMap.put(3, 1);
        yearToBusinessQuarterMap.put(4, 2);
    }

    private final Pattern getValuesFromLabel = Pattern.compile("^GJ (\\d{2})/\\d{2}-Q([1-4])$");
    private final KeyResultBusinessService keyResultBusinessService;
    private final QuarterPersistenceService quarterPersistenceService;
    public YearMonth now;

    public QuarterBusinessService(KeyResultBusinessService keyResultBusinessService,
            QuarterPersistenceService quarterPersistenceService, YearMonth now) {
        this.keyResultBusinessService = keyResultBusinessService;
        this.quarterPersistenceService = quarterPersistenceService;
        this.now = now;
    }

    public Quarter getQuarterById(Long quarterId) {
        return quarterPersistenceService.getQuarterById(quarterId);
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

    protected Quarter getOrCreateQuarter(String label) {
        return quarterPersistenceService.getOrCreateQuarter(label);
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

    public StartEndDateDTO getStartAndEndDateOfKeyresult(long keyResultId) {
        KeyResult keyResult = keyResultBusinessService.getKeyResultById(keyResultId);
        String quarterLabel = keyResult.getObjective().getQuarter().getLabel();

        YearMonth startYearMonth = getYearMonthFromLabel(quarterLabel);
        YearMonth endYearMonth = startYearMonth.plusMonths(2);

        LocalDate startDate = LocalDate.of(startYearMonth.getYear(), startYearMonth.getMonthValue(), 1);
        LocalDate endDate = LocalDate.of(endYearMonth.getYear(), endYearMonth.getMonthValue(),
                endYearMonth.lengthOfMonth());

        return StartEndDateDTO.Builder.builder().withStartDate(startDate).withEndDate(endDate).build();
    }

    protected YearMonth getYearMonthFromLabel(String quarterLabel) {
        Matcher matcher = getValuesFromLabel.matcher(quarterLabel);
        if (!matcher.find()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Label isn't valid");
        }

        int businessYear = Integer.parseInt(matcher.group(1));
        int businessQuarter = Integer.parseInt(matcher.group(2));

        int calendarQuarter = yearToBusinessQuarterMap.get(businessQuarter);
        int calendarYear = businessQuarter > 2 ? businessYear + 1 : businessYear;
        // Add 2000 since year in quarter is in two digit format
        calendarYear += 2000;

        int month = calendarQuarter * 3 - 2;
        return YearMonth.of(calendarYear, month);
    }
}
