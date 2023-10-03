import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckInFormMetricComponent } from './check-in-form-metric.component';
import { UnitTransformationPipe } from '../../../pipes/unit-transformation/unit-transformation.pipe';
import { KeyResultMetric } from '../../../types/model/KeyResultMetric';
import { keyResultMetricMin } from '../../../testData';
import { UnitLabelTransformationPipe } from '../../../pipes/unit-label-transformation/unit-label-transformation.pipe';
import { FormControl, FormGroup, Validators } from '@angular/forms';

describe('CheckInFormComponent', () => {
  let component: CheckInFormMetricComponent;
  let fixture: ComponentFixture<CheckInFormMetricComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CheckInFormMetricComponent, UnitLabelTransformationPipe],
      providers: [UnitTransformationPipe],
    });
    fixture = TestBed.createComponent(CheckInFormMetricComponent);
    component = fixture.componentInstance;
    component.keyResult = keyResultMetricMin as unknown as KeyResultMetric;
    component.dialogForm = new FormGroup({
      value: new FormControl<string>('', [Validators.required]),
      confidence: new FormControl<number>(5, [Validators.required, Validators.min(1), Validators.max(10)]),
    });
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
