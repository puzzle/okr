package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.repository.QuarterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class QuarterService {

    private final QuarterRepository quarterRepository;
    private HashMap<Integer, Integer> quarterMap = yearToBusinessQuarterMap();
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
        String currentQuarter = generateCurrentQuarter();
        int currentQuarterNumber = getBusinessYearQuarter();
        int currentYear = getCurrentYear();
        String futureQuarter = generateFutureQuarterLabel(currentYear, currentQuarterNumber);
        List<Quarter> quarterList = generatePastQuarters(currentYear, currentQuarterNumber);

        if (quarterRepository.findByLabel(currentQuarter) == null) {
            quarterList.add(quarterRepository.save(Quarter.Builder.builder().withLabel(currentQuarter).build()));
            quarterList.add(quarterRepository.save(Quarter.Builder.builder().withLabel(futureQuarter).build()));
        } else {
            quarterList.add(quarterRepository.findByLabel(currentQuarter));
            if (quarterRepository.findByLabel(futureQuarter) == null) {
                quarterList.add(quarterRepository.save(Quarter.Builder.builder().withLabel(futureQuarter).build()));
            } else {
                quarterList.add(quarterRepository.findByLabel(futureQuarter));
            }
        }

        return quarterList;
    }

    public List<Quarter> generatePastQuarters(int currentYear, int currentQuarter) {
        List<Quarter> quarterList = new ArrayList<>();
        int quarter = currentQuarter;
        int year = currentYear;
        for (int i = 0; i < 4; i++) {
            if (quarter == 1) {
                year -= 1;
                quarter = 4;
            } else {
                quarter -= 1;
            }
            String quarterLabel = "GJ " + year + "/" + (year + 1) + "-Q" + quarter;
            if (quarterRepository.findByLabel(quarterLabel) == null) {
                quarterList.add(quarterRepository.save(Quarter.Builder.builder().withLabel(quarterLabel).build()));
            } else {
                quarterList.add(quarterRepository.findByLabel(quarterLabel));
            }
        }
        return quarterList;
    }

    public String generateFutureQuarterLabel(int currentYear, int currentQuarter) {
        if ((currentQuarter == 3) || (currentQuarter == 2) || (currentQuarter == 1)) {
            return "GJ " + currentYear + "/" + (currentYear + 1) + "-Q" + (currentQuarter + 1);
        } else {
            return "GJ " + (currentYear + 1) + "/" + (currentYear + 2) + "-Q" + 1;
        }
    }

    public int getCurrentYear() {
        return calendar.get(Calendar.YEAR) % 100;
    }

    public int getBusinessYearQuarter() {
        int yearQuarter = (calendar.get(Calendar.MONTH) / 3) + 1;
        return quarterMap.get(yearQuarter);
    }

    public String generateCurrentQuarter() {
        int quarterNumber = getBusinessYearQuarter();
        int year = getCurrentYear();
        String currentQuarterLabel;

        if ((quarterNumber == 3) || (quarterNumber == 4)) {
            currentQuarterLabel = "GJ " + (year - 1) + "/" + year + "-Q" + quarterNumber;
        } else {
            currentQuarterLabel = "GJ " + year + "/" + (year + 1) + "-Q" + quarterNumber;
        }
        return currentQuarterLabel;
    }

    public HashMap<Integer, Integer> yearToBusinessQuarterMap() {
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        hashMap.put(1, 3);
        hashMap.put(2, 4);
        hashMap.put(3, 1);
        hashMap.put(4, 2);
        return hashMap;
    }
}
