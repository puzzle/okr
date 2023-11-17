import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ScoringComponent } from './scoring.component';
import { keyResultMetricMinScoring, keyResultOrdinalMinScoring } from '../../testData';
import { Router } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';
import { Zone } from '../../types/enums/Zone';

describe('ScoringComponent', () => {
  let component: ScoringComponent;
  let fixture: ComponentFixture<ScoringComponent>;

  describe('Basic function tests', () => {
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
      component.keyResult = keyResultMetricMinScoring;
      fixture.detectChanges();
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should fill out star if target percentage is over 100', () => {
      component.stretched = true;
      component.ngAfterViewInit();
      expect(component.iconPath).toBe('filled');
    });

    it.each([
      [{ fail: 0, commit: 0, target: 0, className: null, borderClass: 'none' }],
      [{ fail: 100, commit: 0, target: 0, className: 'score-red', borderClass: 'fail' }],
      [{ fail: 100, commit: 100, target: 0, className: 'score-yellow', borderClass: 'commit' }],
      [{ fail: 100, commit: 100, target: 100, className: 'score-green', borderClass: 'target' }],
      [{ fail: 100, commit: 100, target: 101, className: 'score-stretch', borderClass: 'none' }],
    ])('should set styles correctly', async (object: any) => {
      component.targetPercent = object.target;
      component.commitPercent = object.commit;
      component.failPercent = object.fail;

      let color: string | null = component.getScoringColorClassAndSetBorder();
      expect(color).toBe(object.className);

      if (object.borderClass !== 'none') {
        expect(
          fixture.debugElement.query(By.css('[data-testId="' + object.borderClass + '"]')).nativeElement.classList,
        ).toContain('border-right');
      }
    });
  });

  describe('KeyResult metric', () => {
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
      component.keyResult = keyResultMetricMinScoring;
      fixture.detectChanges();
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });

  describe('KeyResult ordinal', () => {
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
      component.keyResult = keyResultOrdinalMinScoring;
      fixture.detectChanges();
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it.each([
      [{ zoneValue: Zone.FAIL, fail: 100, commit: 0, target: 0 }],
      [{ zoneValue: Zone.COMMIT, fail: 100, commit: 100, target: 0 }],
      [{ zoneValue: Zone.TARGET, fail: 100, commit: 100, target: 100 }],
    ])('should set percentages correctly', (object: any) => {
      //Reset component
      component.targetPercent = 0;
      component.commitPercent = 0;
      component.failPercent = 0;

      //Set zone
      component.keyResult.lastCheckIn!.value! = object.zoneValue;
      component.calculatePercentageOrdinal();

      //Verify if percentage was set correctly
      expect(component.failPercent).toBe(object.fail);
      expect(component.commitPercent).toBe(object.commit);
      expect(component.targetPercent).toBe(object.target);
    });
  });
});
