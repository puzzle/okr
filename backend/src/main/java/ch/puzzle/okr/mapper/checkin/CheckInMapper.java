package ch.puzzle.okr.mapper.checkin;

import ch.puzzle.okr.dto.checkin.CheckInDto;
import ch.puzzle.okr.dto.checkin.CheckInMetricDto;
import ch.puzzle.okr.dto.checkin.CheckInOrdinalDto;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.checkin.CheckInMetric;
import ch.puzzle.okr.models.checkin.CheckInOrdinal;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class CheckInMapper {
    private final CheckInMetricMapper checkInMetricMapper;
    private final CheckInOrdinalMapper checkInOrdinalMapper;

    public CheckInMapper(CheckInMetricMapper checkInMetricMapper, CheckInOrdinalMapper checkInOrdinalMapper) {
        this.checkInMetricMapper = checkInMetricMapper;
        this.checkInOrdinalMapper = checkInOrdinalMapper;
    }

    public CheckInDto toDto(CheckIn checkIn) {
        if (checkIn instanceof CheckInMetric checkInMetric) {
            return checkInMetricMapper.toDto(checkInMetric);
        } else if (checkIn instanceof CheckInOrdinal checkInOrdinal) {
            return checkInOrdinalMapper.toDto(checkInOrdinal);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                              String
                                                      .format("The CheckIn %s can't be converted to a metric or ordinal CheckIn",
                                                              checkIn));
        }
    }

    public CheckIn toCheckIn(CheckInDto checkInDto) {
        if (checkInDto instanceof CheckInMetricDto checkInMetricDto) {
            return checkInMetricMapper.toCheckInMetric(checkInMetricDto);
        } else if (checkInDto instanceof CheckInOrdinalDto checkInOrdinalDto) {
            return checkInOrdinalMapper.toCheckInOrdinal(checkInOrdinalDto);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                              String
                                                      .format("The provided CheckInDto %s is neither metric nor ordinal",
                                                              checkInDto));
        }
    }
}
