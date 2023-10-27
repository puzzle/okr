package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.ActionDto;
import ch.puzzle.okr.models.Action;
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
        return new ActionDto(action.getId(), action.getAction(), action.getPriority(), action.isChecked());
    }

    public List<Action> toActions(List<ActionDto> actionDtos) {
        return actionDtos.stream().map(this::toAction).toList();
    }

    public Action toAction(ActionDto actionDto) {
        return Action.Builder.builder().withId(actionDto.id()).withAction(actionDto.action())
                .withPriority(actionDto.priority()).withIsChecked(actionDto.isChecked()).build();
    }
}
