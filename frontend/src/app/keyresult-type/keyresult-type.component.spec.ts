import { ComponentFixture, TestBed } from '@angular/core/testing';
import * as de from '../../assets/i18n/de.json';

import { KeyresultTypeComponent } from './keyresult-type.component';
import { KeyResult } from '../shared/types/model/KeyResult';
import { keyResultMetric, keyResultOrdinal } from '../shared/testData';
import { TranslateTestingModule } from 'ngx-translate-testing';
import { By } from '@angular/platform-browser';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { User } from '../shared/types/model/User';

describe('KeyresultTypeComponent', () => {
  let component: KeyresultTypeComponent;
  let fixture: ComponentFixture<KeyresultTypeComponent>;

  let metricKeyResult: KeyResult = keyResultMetric;
  let ordinalKeyResult: KeyResult = keyResultOrdinal;

  let metricKeyResultForm = new FormGroup({
    title: new FormControl<string>('100% aller Schweizer Kunden betreuen', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(250),
    ]),
    description: new FormControl<string>('Puzzle ITC erledigt die IT-Aufträge für 100% aller Unternehmen.', [
      Validators.maxLength(4096),
    ]),
    owner: new FormControl<User | string | null>(
      { id: 1, firstname: 'firstname', lastname: 'lastname', username: 'username' },
      [Validators.required, Validators.nullValidator],
    ),
    unit: new FormControl<string | null>('PERCENT'),
    baseline: new FormControl<number | null>(30),
    stretchGoal: new FormControl<number | null>(100),
    commitZone: new FormControl<string | null>(null),
    targetZone: new FormControl<string | null>(null),
    stretchZone: new FormControl<string | null>(null),
    keyResultType: new FormControl<string>('metric'),
  });

  let ordinalKeyResultForm = new FormGroup({
    title: new FormControl<string>('100% aller Schweizer Kunden betreuen', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(250),
    ]),
    description: new FormControl<string>('Puzzle ITC erledigt die IT-Aufträge für 100% aller Unternehmen.', [
      Validators.maxLength(4096),
    ]),
    owner: new FormControl<User | string | null>(
      { id: 1, firstname: 'firstname', lastname: 'lastname', username: 'username' },
      [Validators.required, Validators.nullValidator],
    ),
    unit: new FormControl<string | null>(null),
    baseline: new FormControl<number | null>(null),
    stretchGoal: new FormControl<number | null>(null),
    commitZone: new FormControl<string | null>('Commit'),
    targetZone: new FormControl<string | null>('Target'),
    stretchZone: new FormControl<string | null>('Stretch'),
    keyResultType: new FormControl<string>('metric'),
  });

  let emptyKeyResultForm = new FormGroup({
    title: new FormControl<string>('100% aller Schweizer Kunden betreuen', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(250),
    ]),
    description: new FormControl<string>('Puzzle ITC erledigt die IT-Aufträge für 100% aller Unternehmen.', [
      Validators.maxLength(4096),
    ]),
    owner: new FormControl<User | string | null>(
      { id: 1, firstname: 'firstname', lastname: 'lastname', username: 'username' },
      [Validators.required, Validators.nullValidator],
    ),
    unit: new FormControl<string | null>(null),
    baseline: new FormControl<number | null>(null),
    stretchGoal: new FormControl<number | null>(null),
    commitZone: new FormControl<string | null>(null),
    targetZone: new FormControl<string | null>(null),
    stretchZone: new FormControl<string | null>(null),
    keyResultType: new FormControl<string>('metric'),
  });

  describe('Edit Metric', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [KeyresultTypeComponent],
        imports: [
          TranslateTestingModule.withTranslations({
            de: de,
          }),
          ReactiveFormsModule,
        ],
      });
      fixture = TestBed.createComponent(KeyresultTypeComponent);
      component = fixture.componentInstance;
      component.keyResultForm = metricKeyResultForm;
      fixture.detectChanges();
      component.keyresult = metricKeyResult;
      fixture.detectChanges();
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should use values from input', () => {
      expect(component.typeChangeAllowed).toBeTruthy();
      expect(component.isMetric).toBeTruthy();
      expect(component.keyResultForm.value.unit).toEqual('PERCENT');
      expect(component.keyResultForm.value.baseline).toEqual(30);
      expect(component.keyResultForm.value.stretchGoal).toEqual(100);
      expect(component.keyResultForm.value.targetZone).toBeNull();
      expect(component.keyResultForm.value.commitZone).toBeNull();
      expect(component.keyResultForm.value.stretchZone).toBeNull();
    });

    it('should switch type of KeyResult', () => {
      component.isMetric = true;
      component.typeChangeAllowed = true;

      component.switchKeyResultType('metric');
      expect(component.isMetric).toBeTruthy();
      component.switchKeyResultType('ordinal');
      expect(component.isMetric).toBeFalsy();
      component.typeChangeAllowed = false;

      component.switchKeyResultType('metric');
      expect(component.isMetric).toBeFalsy();
    });

    it('should select metric tab', () => {
      component.isMetric = true;

      let activeTab = document.getElementsByClassName('active')[0];
      expect(activeTab.innerHTML).toContain('Metrisch');
    });

    it('should change to ordinal from html click', () => {
      component.typeChangeAllowed = true;

      expect(component.isMetric).toBeTruthy();
      const ordinalTab = fixture.debugElement.query(By.css('[data-testId="ordinalTab"]'));
      ordinalTab.nativeElement.click();
      expect(component.isMetric).toBeFalsy();
    });
  });

  describe('Edit Ordinal', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [KeyresultTypeComponent],
        imports: [
          TranslateTestingModule.withTranslations({
            de: de,
          }),
          ReactiveFormsModule,
        ],
      });
      fixture = TestBed.createComponent(KeyresultTypeComponent);
      component = fixture.componentInstance;
      component.keyResultForm = ordinalKeyResultForm;
      fixture.detectChanges();
      component.keyresult = ordinalKeyResult;
      fixture.detectChanges();
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should use values from input', () => {
      expect(component.typeChangeAllowed).toBeTruthy();
      expect(component.keyResultForm.value.unit).toBeNull();
      expect(component.keyResultForm.value.baseline).toBeNull();
      expect(component.keyResultForm.value.stretchGoal).toBeNull();
      expect(component.keyResultForm.value.commitZone).toEqual('Commit');
      expect(component.keyResultForm.value.targetZone).toEqual('Target');
      expect(component.keyResultForm.value.stretchZone).toEqual('Stretch');
    });

    it('should switch type of KeyResult', () => {
      component.isMetric = true;
      component.typeChangeAllowed = true;

      component.switchKeyResultType('metric');
      expect(component.isMetric).toBeTruthy();
      component.switchKeyResultType('ordinal');
      expect(component.isMetric).toBeFalsy();
      component.typeChangeAllowed = false;

      component.switchKeyResultType('metric');
      expect(component.isMetric).toBeFalsy();
    });

    it('should select ordinal tab', () => {
      component.isMetric = false;
      fixture.detectChanges();

      let activeTab = document.getElementsByClassName('active')[0];
      expect(activeTab.innerHTML).toContain('Ordinal');
    });

    it('should change to metric from html click', () => {
      component.typeChangeAllowed = true;
      component.isMetric = false;

      expect(component.isMetric).toBeFalsy();
      const metricTab = fixture.debugElement.query(By.css('[data-testId="metricTab"]'));
      metricTab.nativeElement.click();
      expect(component.isMetric).toBeTruthy();
    });
  });

  describe('Create', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [KeyresultTypeComponent],
        imports: [
          TranslateTestingModule.withTranslations({
            de: de,
          }),
          ReactiveFormsModule,
        ],
      });
      fixture = TestBed.createComponent(KeyresultTypeComponent);
      component = fixture.componentInstance;
      component.keyResultForm = emptyKeyResultForm;
      fixture.detectChanges();
      component.keyresult = {} as KeyResult;
      fixture.detectChanges();
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should use default values', () => {
      expect(component.keyResultForm.value.unit).toBeNull();
      expect(component.keyResultForm.value.baseline).toBeNull();
      expect(component.keyResultForm.value.stretchGoal).toBeNull();
      expect(component.keyResultForm.value.commitZone).toBeNull();
      expect(component.keyResultForm.value.targetZone).toBeNull();
      expect(component.keyResultForm.value.stretchZone).toBeNull();
    });

    it('should switch type of KeyResult', () => {
      component.isMetric = true;
      component.typeChangeAllowed = true;

      component.switchKeyResultType('metric');
      expect(component.isMetric).toBeTruthy();
      component.switchKeyResultType('ordinal');
      expect(component.isMetric).toBeFalsy();
      component.typeChangeAllowed = false;

      component.switchKeyResultType('metric');
      expect(component.isMetric).toBeFalsy();
    });

    it('should select metric tab', () => {
      component.isMetric = true;

      let activeTab = document.getElementsByClassName('active')[0];
      expect(activeTab.innerHTML).toContain('Metrisch');
    });

    it('should change to ordinal from html click', () => {
      component.typeChangeAllowed = true;

      expect(component.isMetric).toBeTruthy();
      const ordinalTab = fixture.debugElement.query(By.css('[data-testId="ordinalTab"]'));
      ordinalTab.nativeElement.click();
      expect(component.isMetric).toBeFalsy();
    });
  });
});
