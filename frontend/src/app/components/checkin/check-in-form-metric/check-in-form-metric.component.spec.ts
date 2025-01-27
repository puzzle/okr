import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { CheckInFormMetricComponent } from './check-in-form-metric.component';
import {
  checkInMetric,
  keyResultMetric,
  UNIT_CHF,
  UNIT_EUR,
  UNIT_FTE,
  UNIT_NUMBER,
  UNIT_PERCENT
} from '../../../shared/test-data';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { TranslateTestingModule } from 'ngx-translate-testing';
// @ts-ignore
import * as de from '../../../../assets/i18n/de.json';

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
        TranslateTestingModule.withTranslations({
          de: de
        })
      ],
      declarations: [CheckInFormMetricComponent]
    });
    fixture = TestBed.createComponent(CheckInFormMetricComponent);
    component = fixture.componentInstance;
    component.keyResult = keyResultMetric;
    component.checkIn = checkInMetric;
    component.dialogForm = new FormGroup({
      metricValue: new FormControl<string>('', [Validators.required]),
      confidence: new FormControl<number>(5, [Validators.required,
        Validators.min(1),
        Validators.max(10)])
    });
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should format percent correctly', waitForAsync(async() => {
    component.keyResult = {
      ...keyResultMetric,
      unit: UNIT_PERCENT
    };
    expect(component.generateUnitLabel())
      .toEqual('%');
  }));

  it('should format chf correctly', waitForAsync(async() => {
    component.keyResult = {
      ...keyResultMetric,
      unit: UNIT_CHF
    };
    expect(component.generateUnitLabel())
      .toEqual('CHF');
  }));

  it('should format eur correctly', waitForAsync(async() => {
    component.keyResult = {
      ...keyResultMetric,
      unit: UNIT_EUR
    };
    expect(component.generateUnitLabel())
      .toEqual('EUR');
  }));

  it('should format fte correctly', waitForAsync(async() => {
    component.keyResult = {
      ...keyResultMetric,
      unit: UNIT_FTE
    };
    expect(component.generateUnitLabel())
      .toEqual('FTE');
  }));

  it('should format number correctly', waitForAsync(async() => {
    component.keyResult = {
      ...keyResultMetric,
      unit: UNIT_NUMBER
    };
    expect(component.generateUnitLabel())
      .toEqual('');
  }));


  it.each([
    [0,
      true],
    [150,
      true],
    [-23,
      true],
    [12.3,
      true],
    [-100.3,
      true],
    [' 123   ',
      true],
    ['    -123.4   ',
      true],
    ['12 .3',
      false],
    ['1 2',
      false],
    ['',
      false],
    ['asdf',
      false],
    ['a1',
      false],
    ['1a',
      false],
    [null,
      false]
  ])('should correctly validate value input', (value, validity) => {
    component.dialogForm = new FormGroup({
      metricValue: new FormControl<number | undefined>(undefined, [Validators.required,
        Validators.pattern('^\\s*-?\\d+\\.?\\d*\\s*$')])
    });
    component.dialogForm.setValue({ metricValue: value });
    expect(component.dialogForm.valid)
      .toEqual(validity);
  });
});
