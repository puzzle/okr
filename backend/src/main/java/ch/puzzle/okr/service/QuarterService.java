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
    private HashMap<Integer, Integer> quarterMap = fillQuarterMap();
    private Calendar myCal = new GregorianCalendar();


    public QuarterService(QuarterRepository quarterRepository) {
        this.quarterRepository = quarterRepository;
    }

    public Quarter getQuarterById(Long quarterId) {
        if (quarterId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing attribute quarter id");
        }

        return quarterRepository.findById(quarterId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                (String.format("Quarter with id %d not found", quarterId))));
    }

    public List<Quarter> getCurrentQuarters() {

        return createQuarterIfNotExist();


    }

    public List<Quarter> createQuarterIfNotExist() {
        String currentQuarter = generateCurrentQuarter();
        int currentQuarterNumber = getBusinessYearQuarter();
        int currentYear = getCurrentYear();
        String futureQuarter = generateFutureQuarter(currentYear, currentQuarterNumber);
        generatePastQuarters(currentYear, currentQuarterNumber);

        System.out.println(quarterRepository.findByQuarterLabel(currentQuarter));
        if(quarterRepository.findByQuarterLabel(currentQuarter) == null) {
            quarterRepository.save(Quarter.Builder.builder().withLabel(currentQuarter).build());
            quarterRepository.save(Quarter.Builder.builder().withLabel(futureQuarter).build());
        } else {
            if (quarterRepository.findByQuarterLabel(futureQuarter) == null) {
                quarterRepository.save(Quarter.Builder.builder().withLabel(futureQuarter).build());
            }
        }

        return (List<Quarter>) quarterRepository.findAll();
    }

    public void generatePastQuarters(int currentYear, int currentQuarter) {
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
            quarterRepository.save(Quarter.Builder.builder().withLabel(quarterLabel).build());
        }
    }

    public String generateFutureQuarter(int currentYear, int currentQuarter) {
        if ((currentQuarter == 3) || (currentQuarter == 2) || (currentQuarter == 1)) {
            return "GJ " + currentYear + "/" + (currentYear + 1) + "-Q" + (currentQuarter + 1);
        } else {
            return "GJ " + (currentYear + 1) + "/" + (currentYear + 2) + "-Q" + 1;
        }
    }

    public int getCurrentYear() {
        int year = myCal.get(Calendar.YEAR)%100;
        return year;
    }

    public int getBusinessYearQuarter() {
        int yearQuarter = (myCal.get(Calendar.MONTH) / 3) + 1;
        int quarter = quarterMap.get(yearQuarter);
        return quarter;
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
        System.out.println(currentQuarterLabel);

        return currentQuarterLabel;
    }

    public HashMap<Integer, Integer> fillQuarterMap() {
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        hashMap.put(1, 3);
        hashMap.put(2, 4);
        hashMap.put(3, 1);
        hashMap.put(4, 2);
        return hashMap;
    }
}
