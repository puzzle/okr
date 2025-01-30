import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActionPlanComponent, FormControlsOf, Item } from './action-plan.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatDialogRef } from '@angular/material/dialog';
import { CdkDrag, CdkDropList } from '@angular/cdk/drag-drop';
import { ActionService } from '../../services/action.service';
import { of } from 'rxjs';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { DialogService } from '../../services/dialog.service';
import { ConfirmDialogComponent } from '../../shared/dialog/confirm-dialog/confirm-dialog.component';
import { FormArray, FormArrayName, FormGroup, FormGroupDirective } from '@angular/forms';
import { item2, item3, items, minItem } from '../../shared/test-data';

const actionServiceMock = {
  deleteAction: jest.fn()
};

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
        {
          provide: FormArrayName,
          useValue: { control: new FormArray<FormGroup<FormControlsOf<Item>>>([]) }
        }
      ]
    });
    fixture = TestBed.createComponent(ActionPlanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    actionServiceMock.deleteAction.mockReset();
    component.getFormControlArray()
      .clear();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should remove item from action-plan array', () => {
    setFormControlArrayValue(items);
    actionServiceMock.deleteAction.mockReturnValue(of(null));
    jest
      .spyOn(component.dialogService, 'openConfirmDialog')
      .mockReturnValue({ afterClosed: () => of(true) } as MatDialogRef<ConfirmDialogComponent>);

    component.removeAction(0);

    /*
     * expect(actionServiceMock.deleteAction)
     *   .toHaveBeenCalledWith(item1.id);
     */
    expect(component.getFormControlArray())
      .toHaveLength(2);
    expect(component.getFormControlArray()
      .getRawValue())
      .toStrictEqual([item2,
        item3]);
  });

  it('should remove item from action-plan without opening dialog when action has no text and id', () => {
    const dialogSpy = jest.spyOn(component.dialogService, 'open');
    setFormControlArrayValue([minItem]);

    component.removeAction(0);

    expect(component.getFormControlArray())
      .toHaveLength(0);
    expect(dialogSpy)
      .toHaveBeenCalledTimes(0);
    expect(actionServiceMock.deleteAction).not.toHaveBeenCalled();
  });


  it('should add new action with empty text into array', () => {
    component.addNewItem();
    expect(component.getFormControlArray())
      .toHaveLength(1);
    expect(component.getFormControlArray()
      .getRawValue())
      .toStrictEqual([minItem]);
  });

  function setFormControlArrayValue(items: Item[]) {
    component.getFormControlArray()
      .clear();
    items.forEach((item) => {
      component.addNewItem(item);
    });
  }
});
