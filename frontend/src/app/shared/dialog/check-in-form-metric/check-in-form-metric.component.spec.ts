import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckInFormMetricComponent } from './check-in-form-metric.component';

describe('CheckInFormComponent', () => {
  let component: CheckInFormMetricComponent;
  let fixture: ComponentFixture<CheckInFormMetricComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CheckInFormMetricComponent],
    });
    fixture = TestBed.createComponent(CheckInFormMetricComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
