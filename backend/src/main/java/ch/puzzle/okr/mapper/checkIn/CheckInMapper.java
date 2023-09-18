package ch.puzzle.okr.mapper.checkIn;

import ch.puzzle.okr.dto.checkIn.CheckInAbstractDTO;
import ch.puzzle.okr.dto.checkIn.CheckInDto;
import ch.puzzle.okr.models.checkIn.CheckIn;
import ch.puzzle.okr.models.checkIn.CheckInMetric;
import ch.puzzle.okr.models.checkIn.CheckInOrdinal;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
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
                    "The CheckIn " + checkIn + " can't be converted to a metric " + "or ordinal CheckIn");
        }
    }

    public CheckIn toCheckIn(CheckInAbstractDTO checkInDto, Jwt jwt) {
        if (checkInDto.getCheckInType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The provided type of this CheckIn is null");
        } else if (isMetricCheckIn(checkInDto)) {
            return checkInMetricMapper.toCheckInMetric(checkInDto, jwt);
        } else if (isOrdinalCheckIn(checkInDto)) {
            return checkInOrdinalMapper.toCheckInOrdinal(checkInDto, jwt);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The provided CheckInDto is neither metric nor ordinal");
        }
    }

    public boolean isMetricCheckIn(CheckInAbstractDTO checkInDto) {
        return checkInDto.getCheckInType().equals("metric");
    }

    public boolean isOrdinalCheckIn(CheckInAbstractDTO checkInDto) {
        return checkInDto.getCheckInType().equals("ordinal");
    }
}
