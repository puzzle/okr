import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ScoringComponent } from './scoring.component';
import { checkInOrdinal, keyResultMetric, keyResultOrdinal } from '../../testData';
import { Router } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('ScoringComponent', () => {
  let component: ScoringComponent;
  let fixture: ComponentFixture<ScoringComponent>;

  describe('Color and width calculation metric', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ScoringComponent],
        providers: [
          {
            provide: Router,
            useValue: {
              url: '/okr/overview',
            },
          },
        ],
        imports: [HttpClientTestingModule],
      }).compileComponents();

      fixture = TestBed.createComponent(ScoringComponent);
      component = fixture.componentInstance;
      component.keyResult = keyResultMetric;
    });

    it('should set all width to 0 when no lastCheckIn', function () {
      component.keyResult.lastCheckIn = null;
      fixture.detectChanges();
      expect(component.failWidth).toEqual('0');
      expect(component.commitWidth).toEqual('0');
      expect(component.targetWidth).toEqual('0');
    });

    it('should calculate the right values and colors fail zone', async function () {
      keyResultMetric.baseline = 12.0;
      component.keyResult = keyResultMetric;
      fixture.detectChanges();
      await fixture.whenStable();

      expect(component.failWidth).toEqual('76.92307692307693%');
      expect(component.commitWidth).toEqual('0');
      expect(component.targetWidth).toEqual('0');
      expect(component.labelWidth).toEqual('49.00000000000001px');
      expect(component.endLineFail).toEqual('endLine');
      expect(component.endLineCommit).toEqual('');
      expect(component.endLineTarget).toEqual('');
      expect(component.metricLabel).toEqual('% 15');
      expect(component.failColor).toEqual('#BA3838');
      expect(component.commitColor).toEqual('#ffffff');
      expect(component.targetColor).toEqual('#ffffff');
    });

    it('should calculate the right values and colors commit zone', async function () {
      keyResultMetric.baseline = 8.0;
      component.keyResult = keyResultMetric;
      fixture.detectChanges();
      await fixture.whenStable();

      expect(component.failWidth).toEqual('100%');
      expect(component.commitWidth).toEqual('27.941176470588225%');
      expect(component.targetWidth).toEqual('0');
      expect(component.labelWidth).toEqual('81.49852941176471px');
      expect(component.endLineFail).toEqual('');
      expect(component.endLineCommit).toEqual('endLine');
      expect(component.endLineTarget).toEqual('');
      expect(component.metricLabel).toEqual('% 15');
      expect(component.failColor).toEqual('#FFD600');
      expect(component.commitColor).toEqual('#FFD600');
      expect(component.targetColor).toEqual('#ffffff');
    });

    it('should calculate the right values and colors target zone', async function () {
      keyResultMetric.stretchGoal = 17.0;
      component.keyResult = keyResultMetric;
      fixture.detectChanges();
      await fixture.whenStable();

      expect(component.failWidth).toEqual('100%');
      expect(component.commitWidth).toEqual('100%');
      expect(component.targetWidth).toEqual('4.761904761904769%');
      expect(component.labelWidth).toEqual('130.43333333333334px');
      expect(component.endLineFail).toEqual('');
      expect(component.endLineCommit).toEqual('');
      expect(component.endLineTarget).toEqual('endLine');
      expect(component.metricLabel).toEqual('% 15');
      expect(component.failColor).toEqual('#1E8A29');
      expect(component.commitColor).toEqual('#1E8A29');
      expect(component.targetColor).toEqual('#1E8A29');
    });

    it('should calculate the right values and colors stretch zone', async function () {
      keyResultMetric.stretchGoal = 15.0;
      component.keyResult = keyResultMetric;
      fixture.detectChanges();
      await fixture.whenStable();

      expect(component.failWidth).toEqual('100%');
      expect(component.commitWidth).toEqual('100%');
      expect(component.targetWidth).toEqual('100%');
      expect(component.labelWidth).toEqual('191.10000000000002px');
      expect(component.endLineFail).toEqual('');
      expect(component.endLineCommit).toEqual('');
      expect(component.endLineTarget).toEqual('');
      expect(component.metricLabel).toEqual('% 15');
      expect(component.failColor).toEqual('url("../../../../assets/images/scoring-stars.svg")');
      expect(component.commitColor).toEqual('url("../../../../assets/images/scoring-stars.svg")');
      expect(component.targetColor).toEqual('url("../../../../assets/images/scoring-stars.svg")');
      expect(component.iconPath).toEqual('filled');
    });

    it('should handle decreasing KeyResults', async function () {
      keyResultMetric.stretchGoal = 10.0;
      keyResultMetric.baseline = 20.0;
      component.keyResult = keyResultMetric;
      fixture.detectChanges();
      await fixture.whenStable();

      expect(component.failWidth).toEqual('100%');
      expect(component.commitWidth).toEqual('50%');
      expect(component.targetWidth).toEqual('0');
      expect(component.labelWidth).toEqual('95.55000000000001px');
      expect(component.endLineFail).toEqual('');
      expect(component.endLineCommit).toEqual('endLine');
      expect(component.endLineTarget).toEqual('');
      expect(component.metricLabel).toEqual('% 15');
      expect(component.failColor).toEqual('#FFD600');
      expect(component.commitColor).toEqual('#FFD600');
      expect(component.targetColor).toEqual('#ffffff');
    });
  });

  describe('Overview metric', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ScoringComponent],
        providers: [
          {
            provide: Router,
            useValue: {
              url: '/okr/overview',
            },
          },
        ],
        imports: [HttpClientTestingModule],
      }).compileComponents();

      fixture = TestBed.createComponent(ScoringComponent);
      component = fixture.componentInstance;
      component.keyResult = keyResultMetric;
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should set isOverview and zoneWidth', async function () {
      fixture.detectChanges();
      await fixture.whenStable();
      expect(component.zoneWidth).toEqual(63.7);
      expect(component.isOverview).toBeTruthy();
    });
  });

  describe('Color and width calculation ordinal', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ScoringComponent],
        providers: [
          {
            provide: Router,
            useValue: {
              url: '/okr/overview',
            },
          },
        ],
        imports: [HttpClientTestingModule],
      }).compileComponents();

      fixture = TestBed.createComponent(ScoringComponent);
      component = fixture.componentInstance;
      component.keyResult = keyResultOrdinal;
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should set colors fail zone', function () {
      checkInOrdinal.value = 'FAIL';
      component.keyResult.lastCheckIn = checkInOrdinal;
      fixture.detectChanges();
      expect(component.failWidth).toEqual('100%');
      expect(component.commitWidth).toEqual('0');
      expect(component.targetWidth).toEqual('0');
      expect(component.failColor).toEqual('#BA3838');
      expect(component.commitColor).toEqual('#ffffff');
      expect(component.targetColor).toEqual('#ffffff');
    });

    it('should set colors commit zone', function () {
      checkInOrdinal.value = 'COMMIT';
      component.keyResult.lastCheckIn = checkInOrdinal;
      fixture.detectChanges();
      expect(component.failWidth).toEqual('100%');
      expect(component.commitWidth).toEqual('100%');
      expect(component.targetWidth).toEqual('0');
      expect(component.failColor).toEqual('#FFD600');
      expect(component.commitColor).toEqual('#FFD600');
      expect(component.targetColor).toEqual('#ffffff');
    });

    it('should set colors target zone', function () {
      checkInOrdinal.value = 'TARGET';
      component.keyResult.lastCheckIn = checkInOrdinal;
      fixture.detectChanges();
      expect(component.failWidth).toEqual('100%');
      expect(component.commitWidth).toEqual('100%');
      expect(component.targetWidth).toEqual('100%');
      expect(component.failColor).toEqual('#1E8A29');
      expect(component.commitColor).toEqual('#1E8A29');
      expect(component.targetColor).toEqual('#1E8A29');
    });

    it('should set colors stretch zone', function () {
      checkInOrdinal.value = 'STRETCH';
      component.keyResult.lastCheckIn = checkInOrdinal;
      fixture.detectChanges();
      expect(component.failWidth).toEqual('100%');
      expect(component.commitWidth).toEqual('100%');
      expect(component.targetWidth).toEqual('100%');
      expect(component.failColor).toEqual('url("../../../../assets/images/scoring-stars.svg")');
      expect(component.commitColor).toEqual('url("../../../../assets/images/scoring-stars.svg")');
      expect(component.targetColor).toEqual('url("../../../../assets/images/scoring-stars.svg")');
    });
  });

  describe('KeyResultDetail', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ScoringComponent],
        providers: [
          {
            provide: Router,
            useValue: {
              url: '/okr/keyresult/3',
            },
          },
        ],
        imports: [HttpClientTestingModule],
      }).compileComponents();

      fixture = TestBed.createComponent(ScoringComponent);
      component = fixture.componentInstance;
      component.keyResult = keyResultMetric;
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should set isOverview and zoneWidth', async function () {
      fixture.detectChanges();
      await fixture.whenStable();
      expect(component.zoneWidth).toEqual(98.7);
      expect(component.isOverview).toBeFalsy();
    });
  });
});
