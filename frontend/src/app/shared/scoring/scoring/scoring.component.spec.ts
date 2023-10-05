import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ScoringComponent } from './scoring.component';
import { keyResultMetricMin, keyResultMetricMinScoring, keyResultOrdinalMin } from '../../testData';
import { Router } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';

describe('ScoringComponent', () => {
  let component: ScoringComponent;
  let fixture: ComponentFixture<ScoringComponent>;

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
      component.keyResult = keyResultMetricMinScoring;
      fixture.detectChanges();
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should calculate progress correctly', async () => {
      component.calculatePercentageMetric();
      expect(component.targetPercent).toBe(0);
      expect(component.commitPercent).toBe(50);
      expect(component.failPercent).toBe(100);
    });

    xit('should calculate progress correctly', async () => {
      //Prepare data
      component.targetPercent = 100;
      component.commitPercent = 100;
      component.failPercent = 100;

      component.getScoringColorClassAndSetBorder();

      expect(fixture.debugElement.query(By.css('#fail')).classes).toContain('richi');
      expect(component.commitPercent).toBe(50);
      expect(component.failPercent).toBe(100);
    });
  });

  describe('Overview ordinal', () => {
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
      component.keyResult = keyResultMetricMin;
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });
});
