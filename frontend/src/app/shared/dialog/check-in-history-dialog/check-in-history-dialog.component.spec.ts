import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckInHistoryDialogComponent } from './check-in-history-dialog.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('CheckInHistoryDialogComponent', () => {
  let component: CheckInHistoryDialogComponent;
  let fixture: ComponentFixture<CheckInHistoryDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CheckInHistoryDialogComponent],
      imports: [HttpClientTestingModule],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: MatDialogRef, useValue: {} },
      ],
    });
    fixture = TestBed.createComponent(CheckInHistoryDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
