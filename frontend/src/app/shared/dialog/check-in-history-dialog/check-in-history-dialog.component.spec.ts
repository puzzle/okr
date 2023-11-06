import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckInHistoryDialogComponent } from './check-in-history-dialog.component';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { checkInMetric, checkInMetricWriteableFalse, checkInOrdinal, keyResult } from '../../testData';
import { By } from '@angular/platform-browser';

const checkInService = {
  getAllCheckInOfKeyResult: jest.fn(),
};

describe('CheckInHistoryDialogComponent', () => {
  let component: CheckInHistoryDialogComponent;
  let fixture: ComponentFixture<CheckInHistoryDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CheckInHistoryDialogComponent],
      imports: [HttpClientTestingModule, MatDialogModule],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: { keyResult: keyResult } },
        { provide: MatDialogRef, useValue: {} },
      ],
    });
    jest
      .spyOn(checkInService, 'getAllCheckInOfKeyResult')
      .mockReturnValue([checkInMetric, checkInMetricWriteableFalse]);
    fixture = TestBed.createComponent(CheckInHistoryDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should not display edit check-in button if writeable is false', async () => {
    const buttons = fixture.debugElement.queryAll(By.css('button'));
    expect(buttons.length).toBe(1);
  });
});
