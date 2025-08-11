import { ComponentFixture, TestBed } from '@angular/core/testing';
// @ts-ignore
import * as de from '../../../assets/i18n/de.json';
import { KeyResultMetricField, KeyResultTypeComponent, MetricValue } from './key-result-type.component';
import { KeyResult } from '../../shared/types/model/key-result';
import { keyResultMetric, keyResultOrdinal, users } from '../../shared/test-data';
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
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';

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
          TranslateModule.forRoot({
            loader: {
              provide: TranslateLoader,
              useValue: {
                getTranslation: () => of(de)
              }
            }
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
          TranslateModule.forRoot({
            loader: {
              provide: TranslateLoader,
              useValue: {
                getTranslation: () => of(de)
              }
            }
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
          TranslateModule.forRoot({
            loader: {
              provide: TranslateLoader,
              useValue: {
                getTranslation: () => of(de)
              }
            }
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
      targetValue: 0,
      stretchGoal: 5 },
    KeyResultMetricField.BASELINE,
    KeyResultMetricField.TARGET_VALUE],
    [{ baseline: 0,
      targetValue: 0,
      stretchGoal: 5 },
    KeyResultMetricField.STRETCH_GOAL,
    KeyResultMetricField.TARGET_VALUE],
    [{ baseline: 0,
      targetValue: 0,
      stretchGoal: 5 },
    KeyResultMetricField.TARGET_VALUE,
    KeyResultMetricField.STRETCH_GOAL]])('Should call calculateValueForField with the correct enum', (values: MetricValue, changed: KeyResultMetricField, result: KeyResultMetricField) => {
      const spyInstance = jest.spyOn(component, 'calculateValueForField');

      component.calculateValueAfterChanged(values, changed, component.keyResultForm);
      expect(spyInstance)
        .toHaveBeenCalledWith(values, result, component.keyResultForm);
    });

    it.each([
      [{ baseline: NaN,
        targetValue: 8.5,
        stretchGoal: 10 },
      KeyResultMetricField.BASELINE,
      { baseline: 5,
        commitValue: 6.5 }],
      [{ baseline: NaN,
        targetValue: 6.5,
        stretchGoal: 5 },
      KeyResultMetricField.BASELINE,
      { baseline: 10,
        commitValue: 8.5 }],
      [{ baseline: NaN,
        targetValue: -8.5,
        stretchGoal: -10 },
      KeyResultMetricField.BASELINE,
      { baseline: -5,
        commitValue: -6.5 }],
      [{ baseline: NaN,
        targetValue: -6.5,
        stretchGoal: -5 },
      KeyResultMetricField.BASELINE,
      { baseline: -10,
        commitValue: -8.5 }],
      [{ baseline: NaN,
        targetValue: 2,
        stretchGoal: 5 },
      KeyResultMetricField.BASELINE,
      { baseline: -5,
        commitValue: -2 }],
      [{ baseline: NaN,
        targetValue: 5,
        stretchGoal: 10 },
      KeyResultMetricField.BASELINE,
      { baseline: -6.67,
        commitValue: -1.67 }],

      [{ baseline: 5,
        targetValue: NaN,
        stretchGoal: 10 },
      KeyResultMetricField.TARGET_VALUE,
      { targetValue: 8.5,
        commitValue: 6.5 }],
      [{ baseline: 10,
        targetValue: NaN,
        stretchGoal: 5 },
      KeyResultMetricField.TARGET_VALUE,
      { targetValue: 6.5,
        commitValue: 8.5 }],
      [{ baseline: -5,
        targetValue: NaN,
        stretchGoal: -10 },
      KeyResultMetricField.TARGET_VALUE,
      { targetValue: -8.5,
        commitValue: -6.5 }],
      [{ baseline: -10,
        targetValue: NaN,
        stretchGoal: -5 },
      KeyResultMetricField.TARGET_VALUE,
      { targetValue: -6.5,
        commitValue: -8.5 }],
      [{ baseline: -5,
        targetValue: NaN,
        stretchGoal: 5 },
      KeyResultMetricField.TARGET_VALUE,
      { targetValue: 2,
        commitValue: -2 }],
      [{ baseline: 0,
        targetValue: NaN,
        stretchGoal: 5.34 },
      KeyResultMetricField.TARGET_VALUE,
      { targetValue: 3.74,
        commitValue: 1.6 }],

      [{ baseline: 5,
        targetValue: 8.5,
        stretchGoal: NaN },
      KeyResultMetricField.STRETCH_GOAL,
      { stretchGoal: 10,
        commitValue: 6.5 }],
      [{ baseline: 10,
        targetValue: 6.5,
        stretchGoal: NaN },
      KeyResultMetricField.STRETCH_GOAL,
      { stretchGoal: 5,
        commitValue: 8.5 }],
      [{ baseline: -5,
        targetValue: -8.5,
        stretchGoal: NaN },
      KeyResultMetricField.STRETCH_GOAL,
      { stretchGoal: -10,
        commitValue: -6.5 }],
      [{ baseline: -10,
        targetValue: -6.5,
        stretchGoal: NaN },
      KeyResultMetricField.STRETCH_GOAL,
      { stretchGoal: -5,
        commitValue: -8.5 }],
      [{ baseline: -5,
        targetValue: 2,
        stretchGoal: NaN },
      KeyResultMetricField.STRETCH_GOAL,
      { stretchGoal: 5,
        commitValue: -2 }]
    ])('calculateValueForField should calculate correct', (values: MetricValue, targetField: KeyResultMetricField, result: any) => {
      expect(component.calculateValueForField(values, targetField, component.keyResultForm))
        .toEqual(result);
    });

    it('should use default values', () => {
      expect(getValueOfForm(component.keyResultForm, ['metric',
        'unit']).unitName)
        .toEqual('ZAHL');

      expect(getValueOfForm(component.keyResultForm, ['metric',
        'baseline']))
        .toEqual(0);

      expect(getValueOfForm(component.keyResultForm, ['metric',
        'targetValue']))
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
