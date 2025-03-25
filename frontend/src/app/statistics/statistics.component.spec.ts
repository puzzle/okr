import { ComponentFixture, TestBed } from '@angular/core/testing';
import { StatisticsComponent } from './statistics.component';
import { ActivatedRoute, provideRouter, RouterModule } from '@angular/router';
import { EMPTY, of } from 'rxjs';
import { EvaluationService } from '../services/evaluation.service';
import { QuarterFilterComponent } from '../shared/filter/quarter-filter/quarter-filter.component';
import { ApplicationPageComponent } from '../shared/application-page/application-page.component';
import { TeamFilterComponent } from '../shared/filter/team-filter/team-filter.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ApplicationBannerComponent } from '../shared/custom/application-banner/application-banner.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatChipsModule } from '@angular/material/chips';
import { FormsModule } from '@angular/forms';
import { FilterPageChange } from '../shared/types/model/filter-page-change';

const evaluationServiceStub = {
  getStatistics: jest.fn()
};

describe('StatisticsComponent', () => {
  window.ResizeObserver =
      window.ResizeObserver ||
      jest.fn()
        .mockImplementation(() => ({
          disconnect: jest.fn(),
          observe: jest.fn(),
          unobserve: jest.fn()
        }));
  let component: StatisticsComponent;
  let fixture: ComponentFixture<StatisticsComponent>;
  let activatedRouteStub: any;

  beforeEach(async() => {
    evaluationServiceStub.getStatistics.mockReturnValue(of({
      keyResultAmount: 10,
      objectiveAmount: 5,
      keyResultsInFailAmount: 2,
      keyResultsInCommitAmount: 3,
      keyResultsInTargetAmount: 4,
      keyResultsInStretchAmount: 1
    }));

    activatedRouteStub = {
      snapshot: {
        queryParams: {
          quarter: '1',
          teams: '2,3'
        }
      }
    };

    await TestBed.configureTestingModule({
      declarations: [
        StatisticsComponent,
        QuarterFilterComponent,
        ApplicationPageComponent,
        TeamFilterComponent,
        ApplicationBannerComponent
      ],
      imports: [
        RouterModule,
        MatFormFieldModule,
        MatSelectModule,
        MatChipsModule,
        FormsModule
      ],
      providers: [
        { provide: ActivatedRoute,
          useValue: activatedRouteStub },
        { provide: EvaluationService,
          useValue: evaluationServiceStub },
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(StatisticsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  describe('loadOverview', () => {
    it('should assign the statistics observable from evaluationService', (done) => {
      const filterPage = { quarterId: 1,
        teamIds: [2,
          3] } as FilterPageChange;
      component.loadOverview(filterPage);

      component.statistics.subscribe((stat) => {
        expect(stat)
          .toEqual({
            keyResultAmount: 10,
            objectiveAmount: 5,
            keyResultsInFailAmount: 2,
            keyResultsInCommitAmount: 3,
            keyResultsInTargetAmount: 4,
            keyResultsInStretchAmount: 1
          });
        done();
      });
    });

    it('should handle errors and return an EMPTY observable', (done) => {
      // Arrange: simulate an error by making getStatistics return EMPTY
      evaluationServiceStub.getStatistics.mockReturnValueOnce(EMPTY);

      const filterPage = { quarterId: 1,
        teamIds: [2,
          3] } as FilterPageChange;
      component.loadOverview(filterPage);

      // Assert: subscribe and check that no value is emitted (the observable completes without emissions)
      let emitted = false;
      component.statistics.subscribe({
        next: () => emitted = true,
        complete: () => {
          expect(emitted)
            .toBe(false);
          done();
        }
      });
    });
  });

  describe('krObjectiveRelation', () => {
    it('should return the correct ratio when objectiveAmount is non-zero', () => {
      const stats = { keyResultAmount: 10,
        objectiveAmount: 5 } as any;
      expect(component.krObjectiveRelation(stats))
        .toBe(2);
    });

    it('should return 0 when both keyResultAmount and objectiveAmount are 0', () => {
      const stats = { keyResultAmount: 0,
        objectiveAmount: 0 } as any;
      expect(component.krObjectiveRelation(stats))
        .toBe(0);
    });

    it('should return Infinity when objectiveAmount is 0 and keyResultAmount is non-zero', () => {
      const stats = { keyResultAmount: 10,
        objectiveAmount: 0 } as any;
      expect(component.krObjectiveRelation(stats))
        .toBe(Infinity);
    });
  });

  describe('krRelation', () => {
    it('should return correct metric and ordinal ratio for non-zero inputs', () => {
      const result = component.krRelation(2, 3);
      expect(result.metric)
        .toBeCloseTo(2 / 5);
      expect(result.ordinal)
        .toBeCloseTo(3 / 5);
    });

    it('should return 0 for both metric and ordinal when inputs are zero', () => {
      const result = component.krRelation(0, 0);
      expect(result.metric)
        .toBe(0);
      expect(result.ordinal)
        .toBe(0);
    });
  });

  describe('krProgressRelation', () => {
    it('should calculate correct progress ratios and multiplier', () => {
      const stats = {
        keyResultsInFailAmount: 2,
        keyResultsInCommitAmount: 3,
        keyResultsInTargetAmount: 4,
        keyResultsInStretchAmount: 1
      } as any;
      /*
       * Total = 10, so ratios: fail: 0.2, commit: 0.3, target: 0.4, stretch: 0.1.
       * The max ratio is 0.4, so multiplier = 1 / 0.4 = 2.5.
       */
      const result = component.krProgressRelation(stats);
      expect(result.fail)
        .toBeCloseTo(0.2);
      expect(result.commit)
        .toBeCloseTo(0.3);
      expect(result.target)
        .toBeCloseTo(0.4);
      expect(result.stretch)
        .toBeCloseTo(0.1);
      expect(result.multiplier)
        .toBeCloseTo(2.5);
    });

    it('should return Infinity multiplier when all progress amounts are zero', () => {
      const stats = {
        keyResultsInFailAmount: 0,
        keyResultsInCommitAmount: 0,
        keyResultsInTargetAmount: 0,
        keyResultsInStretchAmount: 0
      } as any;
      const result = component.krProgressRelation(stats);
      expect(result.fail)
        .toBe(0);
      expect(result.commit)
        .toBe(0);
      expect(result.target)
        .toBe(0);
      expect(result.stretch)
        .toBe(0);
      expect(result.multiplier)
        .toBe(Infinity);
    });
  });
});
