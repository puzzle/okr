import {ComponentFixture, TestBed} from '@angular/core/testing';
import * as de from '../../assets/i18n/de.json';

import {KeyresultTypeComponent} from './keyresult-type.component';
import {KeyResult} from '../shared/types/model/KeyResult';
import {keyResultMetric, keyResultOrdinal} from '../shared/testData';
import {TranslateTestingModule} from 'ngx-translate-testing';
import {By} from '@angular/platform-browser';

describe('KeyresultTypeComponent', () => {
  let component: KeyresultTypeComponent;
  let fixture: ComponentFixture<KeyresultTypeComponent>;

  let metricKeyResult: KeyResult = keyResultMetric;
  let ordinalKeyResult: KeyResult = keyResultOrdinal;

  describe('Edit Metric', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [KeyresultTypeComponent],
        imports: [
          TranslateTestingModule.withTranslations({
            de: de,
          }),
        ],
      });
      fixture = TestBed.createComponent(KeyresultTypeComponent);
      component = fixture.componentInstance;
      component.keyresult = metricKeyResult;
      fixture.detectChanges();
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should use values from input', () => {
      expect(component.typeChangeAllowed).toBeFalsy();
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
        ],
      });
      fixture = TestBed.createComponent(KeyresultTypeComponent);
      component = fixture.componentInstance;
      component.keyresult = ordinalKeyResult;
      fixture.detectChanges();
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should use values from input', () => {
      expect(component.typeChangeAllowed).toBeFalsy();
      expect(component.isMetric).toBeFalsy();
      expect(component.keyResultForm.value.unit).toBeNull();
      expect(component.keyResultForm.value.baseline).toBeNull();
      expect(component.keyResultForm.value.stretchGoal).toBeNull();
      expect(component.keyResultForm.value.commitZone).toEqual('Grundriss steht');
      expect(component.keyResultForm.value.targetZone).toEqual('Gebäude gebaut');
      expect(component.keyResultForm.value.stretchZone).toEqual('Inneneinrichtung gestaltet');
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

      let activeTab = document.getElementsByClassName('active')[0];
      expect(activeTab.innerHTML).toContain('Ordinal');
    });

    it('should change to metric from html click', () => {
      component.typeChangeAllowed = true;

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
        ],
      });
      fixture = TestBed.createComponent(KeyresultTypeComponent);
      component = fixture.componentInstance;
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
