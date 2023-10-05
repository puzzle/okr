import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ScoringComponent } from './scoring.component';
import { keyResultMetricMin, keyResultOrdinalMin } from '../../testData';
import { Router } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';

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
