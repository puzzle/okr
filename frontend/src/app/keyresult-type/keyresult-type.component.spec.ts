import { ComponentFixture, TestBed } from '@angular/core/testing';
import * as de from '../../assets/i18n/de.json';

import { KeyresultTypeComponent } from './keyresult-type.component';
import { KeyResult } from '../shared/types/model/KeyResult';
import { keyResultMetric, keyResultOrdinal } from '../shared/testData';
import { TranslateTestingModule } from 'ngx-translate-testing';

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
      expect(component.typeForm.value.unit).toEqual('PERCENT');
      expect(component.typeForm.value.stretchGoal).toEqual(100);
      expect(component.typeForm.value.baseline).toEqual(30);
      expect(component.typeForm.value.targetZone).toBeNull();
      expect(component.typeForm.value.commitZone).toBeNull();
      expect(component.typeForm.value.stretchZone).toBeNull();
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
      expect(component.typeForm.value.unit).toBeNull();
      expect(component.typeForm.value.stretchGoal).toBeNull();
      expect(component.typeForm.value.baseline).toBeNull();
      expect(component.typeForm.value.commitZone).toEqual('Grundriss steht');
      expect(component.typeForm.value.targetZone).toEqual('Gebäude gebaut');
      expect(component.typeForm.value.stretchZone).toEqual('Inneneinrichtung gestaltet');
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
      expect(component.typeForm.value.unit).toBeNull();
      expect(component.typeForm.value.targetZone).toBeNull();
      expect(component.typeForm.value.commitZone).toBeNull();
      expect(component.typeForm.value.stretchZone).toBeNull();
      expect(component.typeForm.value.stretchGoal).toBeNull();
      expect(component.typeForm.value.baseline).toBeNull();
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
  });
});
