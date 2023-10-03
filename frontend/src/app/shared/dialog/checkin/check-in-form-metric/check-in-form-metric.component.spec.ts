import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { CheckInFormMetricComponent } from './check-in-form-metric.component';
import { UnitValueTransformationPipe } from '../../../pipes/unit-value-transformation/unit-value-transformation.pipe';
import { checkInMetric, keyResultMetric } from '../../../testData';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { MatDialogModule } from '@angular/material/dialog';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { Unit } from '../../../types/enums/Unit';

describe('CheckInFormComponent', () => {
  let component: CheckInFormMetricComponent;
  let fixture: ComponentFixture<CheckInFormMetricComponent>;
  let loader: HarnessLoader;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        MatDialogModule,
        NoopAnimationsModule,
        MatSelectModule,
        MatInputModule,
        MatRadioModule,
        ReactiveFormsModule,
      ],
      declarations: [CheckInFormMetricComponent],
      providers: [UnitValueTransformationPipe],
    });
    fixture = TestBed.createComponent(CheckInFormMetricComponent);
    component = fixture.componentInstance;
    component.keyResult = keyResultMetric;
    component.checkIn = checkInMetric;
    component.dialogForm = new FormGroup({
      value: new FormControl<string>('', [Validators.required]),
      confidence: new FormControl<number>(5, [Validators.required, Validators.min(1), Validators.max(10)]),
    });
    fixture.detectChanges();
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should format percent correctly', waitForAsync(async () => {
    component.keyResult = { ...keyResultMetric, unit: Unit.PERCENT };
    component.dialogForm.controls['value'].setValue(checkInMetric.value!.toString());
    component.formatValue();
    expect(component.dialogForm.controls['value'].value).toBe(checkInMetric.value + '%');
  }));

  it('should format CHF correctly', waitForAsync(async () => {
    component.keyResult = { ...keyResultMetric, unit: Unit.CHF };
    component.dialogForm.controls['value'].setValue(checkInMetric.value!.toString());
    component.formatValue();
    expect(component.dialogForm.controls['value'].value).toBe(checkInMetric.value + '.-');
  }));

  it('should format FTE correctly', waitForAsync(async () => {
    component.keyResult = { ...keyResultMetric, unit: Unit.FTE };
    component.dialogForm.controls['value'].setValue(checkInMetric.value!.toString());
    component.formatValue();
    expect(component.dialogForm.controls['value'].value).toBe(checkInMetric.value!.toString());
  }));

  it('should parse value correctly', waitForAsync(async () => {
    component.keyResult = { ...keyResultMetric, unit: Unit.CHF };
    component.dialogForm.controls['value'].setValue(checkInMetric.value + '.-');
    expect(component.parseValue()).toBe(checkInMetric.value);
  }));
});
