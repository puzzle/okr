package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.ActionDto;
import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActionMapper {

    private final KeyResultBusinessService keyResultBusinessService;

    public ActionMapper(KeyResultBusinessService keyResultBusinessService) {
        this.keyResultBusinessService = keyResultBusinessService;
    }

    public ActionDto toDto(Action action) {
        return new ActionDto(action.getId(), action.getVersion(), action.getAction(), action.getPriority(),
                action.isChecked(), action.getKeyResult().getId(), action.isWriteable());
    }

    public List<Action> toActions(List<ActionDto> actionDtos, KeyResult keyResult) {
        return actionDtos.stream().map(actionDto -> toAction(actionDto, keyResult)).toList();
    }

    public List<Action> toActions(List<ActionDto> actionDtos) {
        return actionDtos.stream()
                .map(actionDto -> toAction(actionDto, keyResultBusinessService.getEntityById(actionDto.keyResultId())))
                .toList();
    }

    private Action toAction(ActionDto actionDto, KeyResult keyResult) {
        return Action.Builder.builder().withId(actionDto.id()).withVersion(actionDto.version())
                .withAction(actionDto.action()).withPriority(actionDto.priority()).withIsChecked(actionDto.isChecked())
                .withKeyResult(keyResult).build();
    }
}
