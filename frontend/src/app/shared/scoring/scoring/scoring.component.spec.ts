import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ScoringComponent } from './scoring.component';
import { keyResultMetricMinScoring, keyResultMetricMinScoringInversion, keyResultOrdinalMin } from '../../testData';
import { Router } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';

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

    it('should calculate progress correctly', async () => {
      component.keyResult = keyResultMetricMinScoring;
      let percentage = component.calculateCurrentPercentage();
      expect(percentage).toBe(50);
    });

    it('should calculate progress correctly if key result is inversive', async () => {
      component.keyResult = keyResultMetricMinScoringInversion;
      let percentage = component.calculateCurrentPercentage();
      expect(percentage).toBe(75);
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
      component.keyResult = keyResultOrdinalMin;
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });
});
