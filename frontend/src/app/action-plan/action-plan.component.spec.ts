import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActionPlanComponent } from './action-plan.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { CdkDrag, CdkDropList } from '@angular/cdk/drag-drop';
import { ActionService } from '../shared/services/action.service';
import { action1, action2, action3, addedAction } from '../shared/testData';
import { BehaviorSubject, of } from 'rxjs';
import { Action } from '../shared/types/model/Action';

const actionServiceMock = {
  deleteAction: jest.fn(),
};

describe('ActionPlanComponent', () => {
  let component: ActionPlanComponent;
  let fixture: ComponentFixture<ActionPlanComponent>;
  let matDialogRef: MatDialogRef<ActionPlanComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ActionPlanComponent],
      imports: [HttpClientTestingModule, MatDialogModule, CdkDropList, CdkDrag],
      providers: [
        {
          provide: ActionService,
          useValue: actionServiceMock,
        },
        {
          provide: MatDialogRef,
          useValue: matDialogRef,
        },
      ],
    });
    fixture = TestBed.createComponent(ActionPlanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    actionServiceMock.deleteAction.mockReset();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should remove item from actionplan array', () => {
    component.control = new BehaviorSubject<Action[] | null>([action1, action2]);
    actionServiceMock.deleteAction.mockReturnValue(of(null));
    jest.spyOn(component.dialog, 'open').mockReturnValue({ afterClosed: () => of(true) } as MatDialogRef<unknown>);

    component.removeAction(0);

    expect(actionServiceMock.deleteAction).toHaveBeenCalledWith(action1.id);
    expect(component.control.getValue()).toHaveLength(1);
    expect(component.control.getValue()![0]).toBe(action2);
    expect(component.control.getValue()![0].priority).toBe(0);
  });

  it('should remove item from actionplan without opening dialog when action has no text and id', () => {
    const dialogSpy = jest.spyOn(component.dialog, 'open');
    component.control = new BehaviorSubject<Action[] | null>([action3]);

    component.removeAction(0);

    expect(component.control.getValue()!).toHaveLength(0);
    expect(dialogSpy).toHaveBeenCalledTimes(0);
    expect(actionServiceMock.deleteAction).not.toHaveBeenCalled();
  });

  it('should decrease index of active item when index is the same as the one of the removed item', () => {
    jest.spyOn(component.dialog, 'open');
    component.control = new BehaviorSubject<Action[] | null>([action2, action3, action1]);
    component.activeItem = 2;

    component.removeAction(2);
    expect(component.activeItem).toBe(1);
  });

  it('should add new action with empty text into array', () => {
    component.control = new BehaviorSubject<Action[] | null>([]);
    component.keyResultId = addedAction.keyResultId;
    component.addNewAction();
    expect(component.control.getValue()).toHaveLength(1);
    expect(component.control.getValue()![0]).toStrictEqual(addedAction);
  });

  it('should change text of action', () => {
    const testString1 = 'new Text';
    const testString2 = 'Do some stuff';
    component.control = new BehaviorSubject<Action[] | null>([action1, action2]);

    component.changeActionText({ target: { value: testString1 } }, 0);
    component.changeActionText({ target: { value: testString2 } }, 1);

    expect(component.control.getValue()![0].action).toBe(testString1);
    expect(component.control.getValue()![1].action).toBe(testString2);
  });

  it('should decrease index of active item', async () => {
    const keyEvent = new KeyboardEvent('keydown', { key: 'ArrowUp' });
    component.control.next([action1, action2, action3]);
    component.handleKeyDown(keyEvent, 2);

    expect((component.activeItem = 1));
    expect(component.control.getValue()!.toString()).toBe([action1, action3, action2].toString());
    expect(component.control.getValue()![0].priority == 0);
    expect(component.control.getValue()![1].priority == 1);
    expect(component.control.getValue()![2].priority == 2);
  });

  it('should increase index of active item', async () => {
    const keyEvent = new KeyboardEvent('keydown', { key: 'ArrowDown' });
    component.control.next([action1, action2, action3, action1]);
    component.handleKeyDown(keyEvent, 2);

    expect((component.activeItem = 3));
    expect(component.control.getValue()!.toString()).toBe([action1, action3, action1, action3].toString());
    expect(component.control.getValue()![0].priority == 0);
    expect(component.control.getValue()![1].priority == 1);
    expect(component.control.getValue()![2].priority == 2);
    expect(component.control.getValue()![3].priority == 3);
  });

  it('should increase active item index', async () => {
    component.activeItem = 0;
    component.control.next([action1, action2, action3]);
    component.increaseActiveItemWithTab();
    expect(component.activeItem).toBe(1);
  });

  it('should decrease active item index', async () => {
    component.activeItem = 2;
    component.control.next([action1, action2, action3]);
    component.decreaseActiveItemWithShiftTab();
    expect(component.activeItem).toBe(1);
  });
});
