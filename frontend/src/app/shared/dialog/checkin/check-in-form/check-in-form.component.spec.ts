import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { CheckInFormComponent } from './check-in-form.component';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import {
  action1,
  action2,
  checkInMetric,
  checkInOrdinal,
  keyResultActions,
  keyResultMetric,
  keyResultOrdinal,
} from '../../../testData';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { ParseUnitValuePipe } from '../../../pipes/parse-unit-value/parse-unit-value.pipe';
import { CheckInService } from '../../../services/check-in.service';
import { of } from 'rxjs';

const dialogMock = {
  close: jest.fn(),
};

const checkInServiceMock = {
  saveCheckIn: jest.fn(),
};

describe('CheckInFormComponent', () => {
  let component: CheckInFormComponent;
  let fixture: ComponentFixture<CheckInFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        MatDialogModule,
        MatIconModule,
        MatFormFieldModule,
        MatSelectModule,
        ReactiveFormsModule,
        MatInputModule,
        NoopAnimationsModule,
        MatCheckboxModule,
      ],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: { keyResult: {} } },
        { provide: MatDialogRef, useValue: dialogMock },
        { provide: CheckInService, useValue: checkInServiceMock },
        ParseUnitValuePipe,
      ],
      declarations: [CheckInFormComponent],
    });
    fixture = TestBed.createComponent(CheckInFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should save check-in correctly if key result is metric', waitForAsync(async () => {
    component.checkIn = checkInMetric;
    component.keyResult = keyResultMetric;
    component.dialogForm.controls['value'].setValue(checkInMetric?.value!.toString());
    component.dialogForm.controls['confidence'].setValue(checkInMetric.confidence);
    component.dialogForm.controls['changeInfo'].setValue(checkInMetric.changeInfo);
    component.dialogForm.controls['initiatives'].setValue(checkInMetric.initiatives);

    checkInServiceMock.saveCheckIn.mockReturnValue(of(checkInMetric));
    component.saveCheckIn();

    expect(checkInServiceMock.saveCheckIn).toHaveBeenCalledWith({
      id: checkInMetric.id,
      version: checkInMetric.version,
      confidence: checkInMetric.confidence,
      value: checkInMetric.value,
      changeInfo: checkInMetric.changeInfo,
      initiatives: checkInMetric.initiatives,
      keyResultId: keyResultMetric.id,
    });
  }));

  it('should save check-in correctly if key result is ordinal', waitForAsync(async () => {
    component.checkIn = checkInOrdinal;
    component.keyResult = keyResultOrdinal;
    component.dialogForm.controls['value'].setValue(checkInOrdinal?.value as string);
    component.dialogForm.controls['confidence'].setValue(checkInOrdinal.confidence);
    component.dialogForm.controls['changeInfo'].setValue(checkInOrdinal.changeInfo);
    component.dialogForm.controls['initiatives'].setValue(checkInOrdinal.initiatives);

    checkInServiceMock.saveCheckIn.mockReturnValue(of(checkInOrdinal));
    component.saveCheckIn();

    expect(checkInServiceMock.saveCheckIn).toHaveBeenCalledWith({
      id: checkInOrdinal.id,
      version: checkInOrdinal.version,
      confidence: checkInOrdinal.confidence,
      value: checkInOrdinal.value,
      changeInfo: checkInOrdinal.changeInfo,
      initiatives: checkInOrdinal.initiatives,
      keyResultId: keyResultOrdinal.id,
    });
  }));

  it('should set default values if form check-in input is not null', waitForAsync(async () => {
    component.data.checkIn = checkInMetric;
    component.setDefaultValues();
    expect(component.dialogForm.value).toStrictEqual({
      confidence: checkInMetric.confidence,
      value: checkInMetric.value!.toString(),
      changeInfo: checkInMetric.changeInfo,
      initiatives: checkInMetric.initiatives,
      actionList: undefined,
    });
  }));

  it('should set default values if last check-in of key result is not null', waitForAsync(async () => {
    component.keyResult = keyResultOrdinal;
    component.ngOnInit();
    component.setDefaultValues();
    expect(component.dialogForm.value).toStrictEqual({
      confidence: keyResultOrdinal.lastCheckIn!.confidence,
      value: keyResultOrdinal.lastCheckIn!.value,
      changeInfo: '',
      initiatives: '',
      actionList: [],
    });
  }));

  it('should set default values with actionList on KeyResult', waitForAsync(async () => {
    component.keyResult = keyResultActions;
    component.ngOnInit();
    component.setDefaultValues();
    expect(component.dialogForm.value).toStrictEqual({
      confidence: keyResultActions.lastCheckIn!.confidence,
      value: keyResultActions.lastCheckIn!.value.toString(),
      changeInfo: '',
      initiatives: '',
      actionList: [action1, action2],
    });
  }));
});
