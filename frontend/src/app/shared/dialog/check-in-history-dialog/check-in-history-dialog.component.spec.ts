import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckInHistoryDialogComponent } from './check-in-history-dialog.component';

describe('CheckInHistoryDialogComponent', () => {
  let component: CheckInHistoryDialogComponent;
  let fixture: ComponentFixture<CheckInHistoryDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CheckInHistoryDialogComponent],
    });
    fixture = TestBed.createComponent(CheckInHistoryDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
