import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActionPlanComponent, Item } from './action-plan.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatDialogRef } from '@angular/material/dialog';
import { CdkDrag, CdkDropList } from '@angular/cdk/drag-drop';
import { ActionService } from '../../services/action.service';
import { action1, addedAction } from '../../shared/test-data';
import { of } from 'rxjs';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { DialogService } from '../../services/dialog.service';
import { ConfirmDialogComponent } from '../../shared/dialog/confirm-dialog/confirm-dialog.component';
import { FormArrayName, FormGroupDirective } from '@angular/forms';

const actionServiceMock = {
  deleteAction: jest.fn()
};

const minItem: Item = { item: '',
  isChecked: false,
  id: undefined };
const item1: Item = { item: 'item1',
  isChecked: false,
  id: 1 };
const item2: Item = { item: 'item2',
  isChecked: false,
  id: 2 };
const items: Item[] = [item1,
  item2];

describe('ActionPlanComponent', () => {
  let component: ActionPlanComponent;
  let fixture: ComponentFixture<ActionPlanComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ActionPlanComponent],
      imports: [
        HttpClientTestingModule,
        CdkDropList,
        CdkDrag,
        TranslateModule.forRoot()
      ],
      providers: [
        TranslateService,
        DialogService,
        {
          provide: ActionService,
          useValue: actionServiceMock
        },
        FormGroupDirective,
        FormArrayName
      ]
    });
    fixture = TestBed.createComponent(ActionPlanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    actionServiceMock.deleteAction.mockReset();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should remove item from action-plan array', () => {
    component.getFormControlArray()
      .setValue(items);
    actionServiceMock.deleteAction.mockReturnValue(of(null));
    jest
      .spyOn(component.dialogService, 'openConfirmDialog')
      .mockReturnValue({ afterClosed: () => of(true) } as MatDialogRef<ConfirmDialogComponent>);

    component.removeAction(0);

    expect(actionServiceMock.deleteAction)
      .toHaveBeenCalledWith(action1.id);
    expect(component.getFormControlArray())
      .toHaveLength(1);
    expect(component.getFormControlArray()
      .at(0)
      .getRawValue())
      .toBe(item2);
  });

  it('should remove item from action-plan without opening dialog when action has no text and id', () => {
    const dialogSpy = jest.spyOn(component.dialogService, 'open');
    component.getFormControlArray()
      .setValue([minItem]);

    component.removeAction(0);

    expect(component.getFormControlArray())
      .toHaveLength(0);
    expect(dialogSpy)
      .toHaveBeenCalledTimes(0);
    expect(actionServiceMock.deleteAction).not.toHaveBeenCalled();
  });


  it('should add new action with empty text into array', () => {
    // component.control = new BehaviorSubject<Action[] | null>([]);
    component.addNewItem();
    expect(component.getFormControlArray())
      .toHaveLength(1);
    expect(component.getFormControlArray()
      .at(0)
      .getRawValue())
      .toStrictEqual(addedAction);
  });
});
