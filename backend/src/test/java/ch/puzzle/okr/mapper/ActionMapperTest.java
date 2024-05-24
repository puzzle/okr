package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.ActionDto;
import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import ch.puzzle.okr.service.persistence.KeyResultPersistenceService;
import ch.puzzle.okr.service.validation.KeyResultValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ActionMapperTest {

    private static final long ID = 0L;
    private static final int VERSION = 1;
    private static final String ACTION = "action";
    private static final int PRIORITY = 42;
    private static final boolean IS_CHECKED = true;
    private static final boolean IS_WRITEABLE = true;
    public static final long KEY_RESULT_ID = 10L;
    private final KeyResult keyResult = KeyResultMetric.Builder.builder().withId(KEY_RESULT_ID).build();

    private ActionMapper actionMapper;
    @Mock
    private KeyResultBusinessService keyResultBusinessService;
    @Mock
    private KeyResultValidationService validator;
    @Mock
    private KeyResultPersistenceService keyResultPersistenceService;

    @BeforeEach
    void setup() {
        actionMapper = new ActionMapper(keyResultBusinessService);
    }

    @DisplayName("toDto() should map Action to Dto")
    @Test
    void toDtoShouldMapActionToDto() {
        // arrange
        Action action = Action.Builder.builder() //
                .withId(ID) //
                .withVersion(VERSION) //
                .withAction(ACTION) //
                .withPriority(PRIORITY) //
                .withIsChecked(IS_CHECKED) //
                .withKeyResult(keyResult) //
                .build();
        action.setWriteable(IS_WRITEABLE);

        // act
        ActionDto actionDto = actionMapper.toDto(action);

        // assert
        assertNotNull(actionDto);
        assertActionDto(action, actionDto);
    }

    private void assertActionDto(Action expected, ActionDto actual) {
        assertEquals(expected.getId(), actual.id());
        assertEquals(expected.getVersion(), actual.version());
        assertEquals(expected.getAction(), actual.action());
        assertEquals(expected.getPriority(), actual.priority());
        assertEquals(expected.isChecked(), actual.isChecked());
        assertEquals(expected.getKeyResult().getId(), actual.keyResultId());
        assertEquals(expected.isWriteable(), actual.writeable());
    }

    @DisplayName("toActions() should map List of Dtos with KeyResultId to list of Actions.")
    @Test
    void toActionsShouldMapListOfDtosWithKeyResltIdToListOfActions() {
        // arrange
        when(keyResultBusinessService.getEntityById(keyResult.getId())).thenReturn(keyResult);

        ActionDto actionDto = new ActionDto( //
                ID, //
                VERSION, //
                ACTION, //
                PRIORITY, //
                IS_CHECKED, //
                keyResult.getId(), //
                IS_WRITEABLE //
        );

        // act
        List<ActionDto> actionDtoList = List.of(actionDto);
        List<Action> actionList = actionMapper.toActions(actionDtoList);

        // assert
        assertNotNull(actionList);
        assertListOfActionsWithKeyResultId(actionDtoList, actionList);
    }

    @DisplayName("toActions() with KeyResult Parameter should map List of Dtos (without a KeyResultId) to list of Actions")
    @Test
    void toActionsWithKeyResultParameterShouldMapListOfDtosToListOfActions() {
        // arrange
        KeyResult keyResultParameter = KeyResultMetric.Builder.builder().withId(20L).build();
        ActionDto actionDtoWithKeyResultIdIsNull = new ActionDto(ID, //
                VERSION, //
                ACTION, //
                PRIORITY, //
                IS_CHECKED, //
                null, // keyResultId
                IS_WRITEABLE //
        );

        // act
        List<ActionDto> actionDtoList = List.of(actionDtoWithKeyResultIdIsNull);
        List<Action> actionList = actionMapper.toActions(actionDtoList, keyResultParameter);

        // assert
        assertNotNull(actionList);

        // the value of the KeyResultId in the ActionDto is null
        // the value of the KeyResultId in Action is the value of keyResultParameter ( == 20)
        assertListOfActionsAndKeyResultParameter(actionDtoList, actionList, keyResultParameter.getId());
    }

    private void assertListOfActionsWithKeyResultId(List<ActionDto> expectedDtoList, List<Action> actualList) {
        assertListsAndFirstAction(expectedDtoList, actualList);
        assertEquals(expectedDtoList.get(0).keyResultId(), actualList.get(0).getKeyResult().getId());
    }

    private void assertListOfActionsAndKeyResultParameter(List<ActionDto> expectedDtoList, List<Action> actualList,
            Long keyResultId) {
        assertListsAndFirstAction(expectedDtoList, actualList);
        assertEquals(keyResultId, actualList.get(0).getKeyResult().getId());
    }

    private void assertListsAndFirstAction(List<ActionDto> expectedDtoList, List<Action> actualList) {
        assertEquals(expectedDtoList.size(), actualList.size());
        assertEquals(1, actualList.size());

        ActionDto expectedActionDto = expectedDtoList.get(0);
        Action actualAction = actualList.get(0);
        assertNotNull(actualAction);
        assertAction(expectedActionDto, actualAction);
    }

    private void assertAction(ActionDto expected, Action actual) {
        assertEquals(expected.id(), actual.getId());
        assertEquals(expected.version(), actual.getVersion());
        assertEquals(expected.action(), actual.getAction());
        assertEquals(expected.priority(), actual.getPriority());
        assertEquals(expected.isChecked(), actual.isChecked());
        assertFalse(actual.isWriteable()); // TODO immer false?
    }
}
