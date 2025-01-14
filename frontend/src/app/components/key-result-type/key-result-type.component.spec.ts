import { ComponentFixture, TestBed } from '@angular/core/testing';
// @ts-ignore
import * as de from '../../../assets/i18n/de.json';
import { KeyResultTypeComponent } from './key-result-type.component';
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

describe('KeyResultTypeComponent', () => {
  let component: KeyResultTypeComponent;
  let fixture: ComponentFixture<KeyResultTypeComponent>;
  const formGroupDirective = new FormGroupDirective([], []);
  formGroupDirective.form = getKeyResultForm();

  describe('Edit Metric', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [KeyResultTypeComponent],
        imports: [MatAutocompleteModule,
          TranslateTestingModule.withTranslations({
            de: de
          }),
          ReactiveFormsModule],
        providers: [FormGroupDirective,
          { provide: FormGroupDirective,
            useValue: formGroupDirective }]
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

    it('should use values from input', () => {
      expect(component.isTypeChangeAllowed())
        .toBeFalsy();
      expect(component.isMetric)
        .toBeTruthy();

      expect(getValueOfForm(component.keyResultForm, ['metric',
        'unit']))
        .toEqual('PERCENT');
      expect(getValueOfForm(component.keyResultForm, ['metric',
        'baseline']))
        .toEqual(30);
      expect(getValueOfForm(component.keyResultForm, ['metric',
        'stretchGoal']))
        .toEqual(100);

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
        imports: [TranslateTestingModule.withTranslations({
          de: de
        }),
        MatAutocompleteModule,
        ReactiveFormsModule],
        providers: [FormGroupDirective,
          { provide: FormGroupDirective,
            useValue: formGroupDirective }]
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

    it('should use values from input', () => {
      expect(component.isTypeChangeAllowed())
        .toBeTruthy();

      // Default value
      expect(getValueOfForm(component.keyResultForm, ['metric',
        'unit']))
        .toEqual('NUMBER');

      expect(getValueOfForm(component.keyResultForm, ['metric',
        'baseline']))
        .toEqual(0);
      expect(getValueOfForm(component.keyResultForm, ['metric',
        'stretchGoal']))
        .toEqual(0);

      expect(getValueOfForm(component.keyResultForm, ['ordinal',
        'commitZone']))
        .toEqual('Grundriss steht');
      expect(getValueOfForm(component.keyResultForm, ['ordinal',
        'targetZone']))
        .toEqual('Gebäude gebaut');
      expect(getValueOfForm(component.keyResultForm, ['ordinal',
        'stretchZone']))
        .toEqual('Inneneinrichtung gestaltet');
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
        declarations: [KeyResultTypeComponent],
        imports: [MatAutocompleteModule,
          TranslateTestingModule.withTranslations({
            de: de
          }),
          ReactiveFormsModule],
        providers: [FormGroupDirective,
          { provide: FormGroupDirective,
            useValue: formGroupDirective }]
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

    /*
     * it('should have logged in user as owner', waitForAsync(async() => {
     *   const form = getKeyResultForm(testUser)
     *   // form.get('keyResultType')?.setValue('ordinal');
     *   const userServiceSpy = jest.spyOn(userService, 'getUsers');
     *   component.keyResultForm.setValue(getKeyResultForm(testUser));
     *   component.ngOnInit();
     *   fixture.detectChanges();
     *
     *   const formObject = component.keyResultForm.value;
     *   expect(formObject.title)
     *       .toBe('Title');
     *   expect(formObject.description)
     *       .toBe(null);
     *   expect(userServiceSpy)
     *       .toHaveBeenCalled();
     *   expect(component.keyResultForm.controls['owner'].value)
     *       .toBe(testUser);
     *   expect(component.keyResultForm.invalid)
     *       .toBeFalsy();
     * }));
     */

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
