import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ScoringComponent } from './scoring.component';
import { checkInMetric, checkInOrdinal, keyResultMetricMin, keyResultOrdinalMin } from '../../testData';
import { Router } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { KeyResultMetricMin } from '../../types/model/KeyResultMetricMin';
import { CheckInMin } from '../../types/model/CheckInMin';

class ResizeObserverMock {
  observe() {}

  unobserve() {}

  disconnect() {}
}

describe('ScoringComponent', () => {
  //@ts-ignore
  global.ResizeObserver = ResizeObserverMock;
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
      component = fixture.debugElement.componentInstance;
      component.keyResult = keyResultMetricMin;
    });

    it('should set all width to 0 when no lastCheckIn', function () {
      component.keyResult = createKeyResult(10, 20, null);
      fixture.detectChanges();
      expect(component.failWidth).toEqual('0');
      expect(component.commitWidth).toEqual('0');
      expect(component.targetWidth).toEqual('0');
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
      component.keyResult = keyResultMetricMin;
    });

    it('should create', () => {
      expect(component).toBeTruthy();
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

function createKeyResult(baseline: number, stretchGoal: number, lastCheckIn: CheckInMin | null) {
  return {
    id: 201,
    title: 'Have more chocolate in office',
    keyResultType: 'metric',
    unit: '%',
    baseline: baseline,
    stretchGoal: stretchGoal,
    lastCheckIn: lastCheckIn,
    type: 'keyResult',
  } as KeyResultMetricMin;
}
