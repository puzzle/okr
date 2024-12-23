package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.userokrdata.UserKeyResultDataDto;
import ch.puzzle.okr.dto.userokrdata.UserOkrDataDto;
import ch.puzzle.okr.models.keyresult.KeyResult;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class UserOkrDataMapper {

    public UserOkrDataDto toDto(List<KeyResult> keyResults) {
        return new UserOkrDataDto(toUserKeyResultDataDtos(keyResults));
    }

    private List<UserKeyResultDataDto> toUserKeyResultDataDtos(List<KeyResult> keyResults) {
        return keyResults
                .stream() //
                .map(keyResult -> new UserKeyResultDataDto( //
                                                           keyResult.getId(),
                                                           keyResult.getTitle(), //
                                                           keyResult.getObjective().getId(),
                                                           keyResult.getObjective().getTitle() //
                )) //
                .toList();
    }
}