package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.repository.QuarterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.StreamSupport;

@Service
public class QuarterService {

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
        int businessYearQuarter = getBusinessYearQuarter();
        int firstYear = getCurrentYear();
        String currentQuarterLabel = generateQuarterLabel(firstYear, businessYearQuarter);

        List<String> futureQuarter = getFutureQuarterLabels(firstYear, businessYearQuarter, 1);
        List<String> pastQuarter = getPastQuarters(firstYear, businessYearQuarter, 4);

        List<String > quarterLabelList = new ArrayList<>();
        quarterLabelList.add(currentQuarterLabel);
        quarterLabelList.addAll(futureQuarter);
        quarterLabelList.addAll(pastQuarter);

        List<Quarter> quarterList = quarterLabelList.stream().map(Quarter::new).toList();

        return toList(quarterRepository.saveAll(quarterList));
    }

    public List<String> getPastQuarters(int currentYear, int currentQuarter, int amount) {
        List<String> quarterList = new ArrayList<>();
        int quarterNumber = currentQuarter;
        int year = currentYear - 1;
        for (int i = 0; i < amount; i++) {
            quarterNumber--;
            if (quarterNumber < 1) {
                quarterNumber = 4;
                year--;
            }
            quarterList.add(generateQuarterLabel(year, quarterNumber));
        }
        return quarterList;
    }

    public List<String> getFutureQuarterLabels(int currentYear, int currentQuarter, int amount) {
        List<String> quarterList = new ArrayList<>();
        int quarterNumber = currentQuarter;
        int year = currentYear;
        for (int i = 0; i < amount; i++) {
            quarterNumber++;
            if (quarterNumber > 4) {
                quarterNumber = 1;
                year++;
            }
            quarterList.add(generateQuarterLabel(year, quarterNumber));
        }
        return quarterList;
    }

    public int getCurrentYear() {
        return calendar.get(Calendar.YEAR) % 100;
    }

    public String generateQuarterLabel(int firstYear, int currentQuarter) {
        int secondYear = getSecondYear(firstYear, currentQuarter);
        return "GJ " + Math.min(firstYear, secondYear) + "/" + Math.max(firstYear, secondYear) + "-Q" + currentQuarter;
    }

    public int getBusinessYearQuarter() {
        int yearQuarter = calendar.get(Calendar.MONTH) / 3 + 1;
        return yearToBusinessQuarterMap().get(yearQuarter);
    }

    public Map<Integer, Integer> yearToBusinessQuarterMap() {
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        hashMap.put(1, 3);
        hashMap.put(2, 4);
        hashMap.put(3, 1);
        hashMap.put(4, 2);
        return hashMap;
    }

    public int getSecondYear(int firstYear, int businessQuarter) {
        return businessQuarter > 2 ? firstYear - 1 : firstYear + 1;
    }

    public <T> List<T> toList(final Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .toList();
    }
}
