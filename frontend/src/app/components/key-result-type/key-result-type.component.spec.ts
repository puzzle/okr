import { ComponentFixture, TestBed } from '@angular/core/testing';
// @ts-ignore
import * as de from '../../../assets/i18n/de.json';
import { KeyResultMetricField, KeyResultTypeComponent, MetricValue } from './key-result-type.component';
import { KeyResult } from '../../shared/types/model/key-result';
import { keyResultMetric, keyResultOrdinal, users } from '../../shared/test-data';
import { TranslateTestingModule } from 'ngx-translate-testing';
import { By } from '@angular/platform-browser';
import { FormGroupDirective, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { of } from 'rxjs';
import { getKeyResultForm } from '../../shared/constant-library';
import { KeyResultOrdinal } from '../../shared/types/model/key-result-ordinal';
import { getValueOfForm } from '../../shared/common';
import { KeyResultMetric } from '../../shared/types/model/key-result-metric';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ErrorComponent } from '../../shared/custom/error/error.component';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

describe('KeyResultTypeComponent', () => {
  let component: KeyResultTypeComponent;
  let fixture: ComponentFixture<KeyResultTypeComponent>;
  const formGroupDirective = new FormGroupDirective([], []);
  formGroupDirective.form = getKeyResultForm();

  describe('Edit Metric', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [KeyResultTypeComponent],
        imports: [
          MatAutocompleteModule,
          MatFormFieldModule,
          TranslateTestingModule.withTranslations({
            de: de
          }),
          ReactiveFormsModule
        ],
        providers: [
          FormGroupDirective,
          provideRouter([]),
          provideHttpClient(),
          provideHttpClientTesting(),
          { provide: FormGroupDirective,
            useValue: formGroupDirective }
        ]
      });
      fixture = TestBed.createComponent(KeyResultTypeComponent);
      component = fixture.componentInstance;
      component.keyResultForm = getKeyResultForm();
      component.keyResult = { ...keyResultMetric } as KeyResultMetric;
      component.users = of(users);

      fixture.detectChanges();
    });

    it('should create', () => {
      expect(component)
        .toBeTruthy();
    });


    it('should switch type of key-result', () => {
      jest.spyOn(component, 'isTypeChangeAllowed')
        .mockReturnValue(true);

      component.switchKeyResultType('metric');
      expect(component.isMetric)
        .toBeTruthy();

      component.switchKeyResultType('ordinal');
      expect(component.isMetric())
        .toBeFalsy();

      jest.spyOn(component, 'isTypeChangeAllowed')
        .mockReturnValue(false);

      component.switchKeyResultType('metric');
      expect(component.isMetric())
        .toBeFalsy();
    });

    it('should select metric tab', () => {
      jest.spyOn(component, 'isMetric')
        .mockReturnValue(true);

      const activeTab = document.getElementsByClassName('active')[0];
      expect(activeTab.innerHTML)
        .toContain('Metrisch');
    });

    it('should change to ordinal from html click', () => {
      jest.spyOn(component, 'isTypeChangeAllowed')
        .mockReturnValue(true);

      expect(component.isMetric())
        .toBeTruthy();
      const ordinalTab = fixture.debugElement.query(By.css('[data-testId="ordinal-tab"]'));
      ordinalTab.nativeElement.click();
      expect(component.isMetric())
        .toBeFalsy();
    });
  });

  describe('Edit Ordinal', () => {
    const form = getKeyResultForm();
    form.get('keyResultType')
      ?.setValue('ordinal');
    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [KeyResultTypeComponent],
        imports: [
          TranslateTestingModule.withTranslations({
            de: de
          }),
          MatFormFieldModule,
          MatAutocompleteModule,
          ReactiveFormsModule
        ],
        providers: [
          provideRouter([]),
          provideHttpClient(),
          provideHttpClientTesting(),
          FormGroupDirective,
          { provide: FormGroupDirective,
            useValue: formGroupDirective }
        ]
      });
      fixture = TestBed.createComponent(KeyResultTypeComponent);
      component = fixture.componentInstance;
      component.keyResultForm = form;
      component.keyResult = { ...keyResultOrdinal,
        lastCheckIn: null } as KeyResultOrdinal;
      fixture.detectChanges();
    });

    it('should create', () => {
      expect(component)
        .toBeTruthy();
    });

    it('should select ordinal tab', () => {
      jest.spyOn(component, 'isMetric')
        .mockReturnValue(false);
      fixture.detectChanges();

      const activeTab = document.getElementsByClassName('active')[0];
      expect(activeTab.innerHTML)
        .toContain('Ordinal');
    });

    it('should change to metric from html click', () => {
      jest.spyOn(component, 'isTypeChangeAllowed')
        .mockReturnValue(true);

      expect(component.isMetric())
        .toBeFalsy();
      const metricTab = fixture.debugElement.query(By.css('[data-testId="metric-tab"]'));
      metricTab.nativeElement.click();
      expect(component.isMetric())
        .toBeTruthy();
    });
  });

  describe('Create', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [KeyResultTypeComponent,
          ErrorComponent],
        imports: [
          MatAutocompleteModule,
          TranslateTestingModule.withTranslations({
            de: de
          }),
          ReactiveFormsModule,
          MatFormFieldModule
        ],
        providers: [
          FormGroupDirective,
          provideRouter([]),
          provideHttpClient(),
          provideHttpClientTesting(),
          { provide: FormGroupDirective,
            useValue: formGroupDirective }
        ]
      });
      fixture = TestBed.createComponent(KeyResultTypeComponent);
      component = fixture.componentInstance;
      component.keyResultForm = getKeyResultForm();
      fixture.detectChanges();
      component.keyResult = {} as KeyResult;
      fixture.detectChanges();
    });

    it('should create', () => {
      expect(component)
        .toBeTruthy();
    });

    it.each([[{ baseline: 0,
      targetGoal: 0,
      stretchGoal: 5 },
    KeyResultMetricField.BASELINE,
    KeyResultMetricField.TARGET_GOAL],
    [{ baseline: 0,
      targetGoal: 0,
      stretchGoal: 5 },
    KeyResultMetricField.STRETCH_GOAL,
    KeyResultMetricField.TARGET_GOAL],
    [{ baseline: 0,
      targetGoal: 0,
      stretchGoal: 5 },
    KeyResultMetricField.TARGET_GOAL,
    KeyResultMetricField.STRETCH_GOAL]])('Should call calculateValueForField with the correct enum', (values: MetricValue, changed: KeyResultMetricField, result: KeyResultMetricField) => {
      const spyInstance = jest.spyOn(component, 'calculateValueForField');

      component.calculateValueAfterChanged(values, changed);
      expect(spyInstance)
        .toHaveBeenCalledWith(values, result);
    });

    it.each([
      [{ baseline: NaN,
        targetGoal: 8.5,
        stretchGoal: 10 },
      KeyResultMetricField.BASELINE,
      { baseline: 5 }],
      [{ baseline: NaN,
        targetGoal: 6.5,
        stretchGoal: 5 },
      KeyResultMetricField.BASELINE,
      { baseline: 10 }],
      [{ baseline: NaN,
        targetGoal: -8.5,
        stretchGoal: -10 },
      KeyResultMetricField.BASELINE,
      { baseline: -5 }],
      [{ baseline: NaN,
        targetGoal: -6.5,
        stretchGoal: -5 },
      KeyResultMetricField.BASELINE,
      { baseline: -10 }],
      [{ baseline: NaN,
        targetGoal: 2,
        stretchGoal: 5 },
      KeyResultMetricField.BASELINE,
      { baseline: -5 }],
      [{ baseline: NaN,
        targetGoal: 5,
        stretchGoal: 10 },
      KeyResultMetricField.BASELINE,
      { baseline: -6.67 }],

      [{ baseline: 5,
        targetGoal: NaN,
        stretchGoal: 10 },
      KeyResultMetricField.TARGET_GOAL,
      { targetGoal: 8.5 }],
      [{ baseline: 10,
        targetGoal: NaN,
        stretchGoal: 5 },
      KeyResultMetricField.TARGET_GOAL,
      { targetGoal: 6.5 }],
      [{ baseline: -5,
        targetGoal: NaN,
        stretchGoal: -10 },
      KeyResultMetricField.TARGET_GOAL,
      { targetGoal: -8.5 }],
      [{ baseline: -10,
        targetGoal: NaN,
        stretchGoal: -5 },
      KeyResultMetricField.TARGET_GOAL,
      { targetGoal: -6.5 }],
      [{ baseline: -5,
        targetGoal: NaN,
        stretchGoal: 5 },
      KeyResultMetricField.TARGET_GOAL,
      { targetGoal: 2 }],
      [{ baseline: 0,
        targetGoal: NaN,
        stretchGoal: 5.34 },
      KeyResultMetricField.TARGET_GOAL,
      { targetGoal: 3.74 }],

      [{ baseline: 5,
        targetGoal: 8.5,
        stretchGoal: NaN },
      KeyResultMetricField.STRETCH_GOAL,
      { stretchGoal: 10 }],
      [{ baseline: 10,
        targetGoal: 6.5,
        stretchGoal: NaN },
      KeyResultMetricField.STRETCH_GOAL,
      { stretchGoal: 5 }],
      [{ baseline: -5,
        targetGoal: -8.5,
        stretchGoal: NaN },
      KeyResultMetricField.STRETCH_GOAL,
      { stretchGoal: -10 }],
      [{ baseline: -10,
        targetGoal: -6.5,
        stretchGoal: NaN },
      KeyResultMetricField.STRETCH_GOAL,
      { stretchGoal: -5 }],
      [{ baseline: -5,
        targetGoal: 2,
        stretchGoal: NaN },
      KeyResultMetricField.STRETCH_GOAL,
      { stretchGoal: 5 }]
    ])('calculateValueForField should calculate correct', (values: MetricValue, targetField: KeyResultMetricField, result: any) => {
      expect(component.calculateValueForField(values, targetField))
        .toEqual(result);
    });

    it('should use default values', () => {
      expect(getValueOfForm(component.keyResultForm, ['metric',
        'unit']))
        .toEqual('NUMBER');

      expect(getValueOfForm(component.keyResultForm, ['metric',
        'baseline']))
        .toEqual(0);

      expect(getValueOfForm(component.keyResultForm, ['metric',
        'targetGoal']))
        .toEqual(0);

      expect(getValueOfForm(component.keyResultForm, ['metric',
        'stretchGoal']))
        .toEqual(0);

      expect(getValueOfForm(component.keyResultForm, ['ordinal',
        'commitZone']))
        .toEqual('');
      expect(getValueOfForm(component.keyResultForm, ['ordinal',
        'targetZone']))
        .toEqual('');
      expect(getValueOfForm(component.keyResultForm, ['ordinal',
        'stretchZone']))
        .toEqual('');
    });

    it('should switch type of key-result', () => {
      jest.spyOn(component, 'isTypeChangeAllowed')
        .mockReturnValue(true);

      component.switchKeyResultType('metric');
      expect(component.isMetric())
        .toBeTruthy();
      component.switchKeyResultType('ordinal');
      expect(component.isMetric())
        .toBeFalsy();
      jest.spyOn(component, 'isTypeChangeAllowed')
        .mockReturnValue(false);

      component.switchKeyResultType('metric');
      expect(component.isMetric())
        .toBeFalsy();
    });

    it('should select metric tab', () => {
      jest.spyOn(component, 'isMetric')
        .mockReturnValue(true);

      const activeTab = document.getElementsByClassName('active')[0];
      expect(activeTab.innerHTML)
        .toContain('Metrisch');
    });

    it('should change to ordinal from html click', () => {
      jest.spyOn(component, 'isTypeChangeAllowed')
        .mockReturnValue(true);

      expect(component.isMetric())
        .toBeTruthy();
      const ordinalTab = fixture.debugElement.query(By.css('[data-testId="ordinal-tab"]'));
      ordinalTab.nativeElement.click();
      expect(component.isMetric())
        .toBeFalsy();
    });
  });
});
