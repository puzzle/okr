import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { CheckInFormMetricComponent } from './check-in-form-metric.component';
import { UnitValueTransformationPipe } from '../../../pipes/unit-value-transformation/unit-value-transformation.pipe';
import { checkInMetric, keyResultMetric } from '../../../testData';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { Unit } from '../../../types/enums/Unit';
import { ParseUnitValuePipe } from '../../../pipes/parse-unit-value/parse-unit-value.pipe';

describe('CheckInFormComponent', () => {
  let component: CheckInFormMetricComponent;
  let fixture: ComponentFixture<CheckInFormMetricComponent>;

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
      declarations: [CheckInFormMetricComponent, UnitValueTransformationPipe],
      providers: [UnitValueTransformationPipe, ParseUnitValuePipe],
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
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should format percent correctly', waitForAsync(async () => {
    component.keyResult = { ...keyResultMetric, unit: Unit.PERCENT };
    expect(component.generateUnitLabel()).toEqual('%');
  }));

  it('should format chf correctly', waitForAsync(async () => {
    component.keyResult = { ...keyResultMetric, unit: Unit.CHF };
    expect(component.generateUnitLabel()).toEqual('CHF');
  }));

  it('should format fte correctly', waitForAsync(async () => {
    component.keyResult = { ...keyResultMetric, unit: Unit.FTE };
    expect(component.generateUnitLabel()).toEqual('FTE');
  }));

  it('should format number correctly', waitForAsync(async () => {
    component.keyResult = { ...keyResultMetric, unit: Unit.NUMBER };
    expect(component.generateUnitLabel()).toEqual('');
  }));
});
