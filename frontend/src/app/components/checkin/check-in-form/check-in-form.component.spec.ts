import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { CheckInFormComponent } from './check-in-form.component';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import {
  action2,
  checkInMetric,
  checkInOrdinal,
  keyResultActions,
  keyResultMetric,
  keyResultOrdinal
} from '../../../shared/test-data';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { CheckInService } from '../../../services/check-in.service';
import { of } from 'rxjs';
import { ActionService } from '../../../services/action.service';
// @ts-ignore
import * as de from '../../../../assets/i18n/de.json';
import { ConfidenceComponent } from '../../confidence/confidence.component';
import { MatSliderModule } from '@angular/material/slider';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import {
  DialogTemplateCoreComponent
} from '../../../shared/custom/dialog-template-core/dialog-template-core.component';
import { MatDividerModule } from '@angular/material/divider';
import { Item } from '../../action-plan/action-plan.component';
import { Zone } from '../../../shared/types/enums/zone';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';

const dialogMock = {
  close: jest.fn()
};

const checkInServiceMock = {
  saveCheckIn: jest.fn()
};

const actionServiceMock = {
  updateActions: jest.fn()
};

describe('CheckInFormComponent', () => {
  let component: CheckInFormComponent;
  let fixture: ComponentFixture<CheckInFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        MatDialogModule,
        MatIconModule,
        MatFormFieldModule,
        MatSelectModule,
        ReactiveFormsModule,
        MatInputModule,
        NoopAnimationsModule,
        MatCheckboxModule,
        MatSliderModule,
        FormsModule,
        ReactiveFormsModule,
        TranslateModule.forRoot({
          loader: {
            provide: TranslateLoader,
            useValue: {
              getTranslation: () => of(de)
            }
          }
        }),
        MatDividerModule
      ],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        {
          provide: MAT_DIALOG_DATA,
          useValue: { keyResult: {} }
        },
        {
          provide: MatDialogRef,
          useValue: dialogMock
        },
        {
          provide: CheckInService,
          useValue: checkInServiceMock
        },
        {
          provide: ActionService,
          useValue: actionServiceMock
        }
      ],
      declarations: [CheckInFormComponent,
        DialogTemplateCoreComponent,
        ConfidenceComponent]
    });
    fixture = TestBed.createComponent(CheckInFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should save check-in correctly if key-result is metric', waitForAsync(async() => {
    component.checkIn = checkInMetric;
    component.keyResult = keyResultMetric;
    component.dialogForm.controls['metricValue'].setValue(checkInMetric?.value);
    component.dialogForm.controls['confidence'].setValue(checkInMetric.confidence);
    component.dialogForm.controls['changeInfo'].setValue(checkInMetric.changeInfo);
    component.dialogForm.controls['initiatives'].setValue(checkInMetric.initiatives);

    checkInServiceMock.saveCheckIn.mockReturnValue(of(checkInMetric));
    actionServiceMock.updateActions.mockReturnValue(of(action2));
    component.saveCheckIn();

    expect(checkInServiceMock.saveCheckIn)
      .toHaveBeenCalledWith({
        id: checkInMetric.id,
        version: checkInMetric.version,
        confidence: checkInMetric.confidence,
        changeInfo: checkInMetric.changeInfo,
        initiatives: checkInMetric.initiatives,
        value: checkInMetric.value,
        keyResultId: keyResultMetric.id
      });
    expect(actionServiceMock.updateActions)
      .toHaveBeenCalled();
  }));

  it('should save check-in correctly if key-result is ordinal', waitForAsync(async() => {
    component.checkIn = checkInOrdinal;
    component.keyResult = keyResultOrdinal;
    component.dialogForm.controls['ordinalZone'].setValue(checkInOrdinal?.zone as Zone);
    component.dialogForm.controls['confidence'].setValue(checkInOrdinal.confidence);
    component.dialogForm.controls['changeInfo'].setValue(checkInOrdinal.changeInfo);
    component.dialogForm.controls['initiatives'].setValue(checkInOrdinal.initiatives);

    checkInServiceMock.saveCheckIn.mockReturnValue(of(checkInOrdinal));
    actionServiceMock.updateActions.mockReturnValue(of(action2));
    component.saveCheckIn();

    expect(checkInServiceMock.saveCheckIn)
      .toHaveBeenCalledWith({
        id: checkInOrdinal.id,
        version: checkInOrdinal.version,
        confidence: checkInOrdinal.confidence,
        zone: checkInOrdinal.zone,
        changeInfo: checkInOrdinal.changeInfo,
        initiatives: checkInOrdinal.initiatives,
        keyResultId: keyResultOrdinal.id
      });

    expect(actionServiceMock.updateActions)
      .toHaveBeenCalled();
  }));

  it('should set default values if form check-in input is not null', waitForAsync(async() => {
    component.data.checkIn = checkInMetric;
    component.setDefaultValues();
    expect(component.dialogForm.value)
      .toStrictEqual({
        confidence: checkInMetric.confidence,
        changeInfo: checkInMetric.changeInfo,
        initiatives: checkInMetric.initiatives,
        actionList: []
      });
  }));

  it('should set default values if last check-in of key-result is not null', waitForAsync(async() => {
    component.keyResult = keyResultOrdinal;
    component.ngOnInit();
    component.setDefaultValues();
    expect(component.dialogForm.value)
      .toStrictEqual({
        confidence: keyResultOrdinal.lastCheckIn!.confidence,
        changeInfo: '',
        initiatives: '',
        actionList: []
      });
  }));

  it('should set default values with actionList on key-result', waitForAsync(async() => {
    component.keyResult = keyResultActions;
    component.ngOnInit();
    component.setDefaultValues();
    expect(component.dialogForm.value)
      .toStrictEqual(expect.objectContaining({
        confidence: keyResultActions.lastCheckIn!.confidence,
        changeInfo: '',
        initiatives: ''
      }));
    expect(component.dialogForm.getRawValue().actionList?.map((action: Item) => action.item))
      .toStrictEqual(['Drucker kaufen',
        'Blätter kaufen']);
  }));

  it('should call action-service when saving check-in', waitForAsync(async() => {
    checkInServiceMock.saveCheckIn.mockReturnValue(of(true));
    actionServiceMock.updateActions.mockReturnValue(of(true));

    component.keyResult = keyResultActions;
    component.ngOnInit();
    component.setDefaultValues();
    component.saveCheckIn();
    expect(actionServiceMock.updateActions)
      .toHaveBeenCalled();
  }));
});
