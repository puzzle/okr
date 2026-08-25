import { ComponentFixture, TestBed } from '@angular/core/testing';
import { QuarterFilterComponent } from './quarter-filter.component';
import { QuarterService } from '../../../services/quarter.service';
import { RefreshDataService } from '../../../services/refresh-data.service';
import { ActivatedRoute, Router } from '@angular/router';
import { BehaviorSubject, of } from 'rxjs';
import { getValueFromQuery } from '../../common';
import { Quarter } from '../../types/model/quarter';

jest.mock('../../common', () => ({
  getValueFromQuery: jest.fn()
}));

const mockQuarters = [new Quarter(
  2, '23.02.2025', new Date(), new Date(), false
),
new Quarter(
  5, 'Q2 - 2025', new Date(), new Date(), false
),
new Quarter(
  7, 'Q3 - 2025', new Date(), new Date(), false
)];

const mockRouter = { navigate: jest.fn() };
const mockQuarterService = { getAllQuarters: jest.fn()
  .mockReturnValue(of(mockQuarters)) };

describe('QuarterFilterComponent', () => {
  let component: QuarterFilterComponent;
  let fixture: ComponentFixture<QuarterFilterComponent>;
  let mockActivatedRoute: { queryParams: BehaviorSubject<any> };

  beforeEach(async() => {
    (getValueFromQuery as jest.Mock).mockReset()
      .mockReturnValue([]);

    mockActivatedRoute = {
      queryParams: new BehaviorSubject<any>({})
    };

    await TestBed.configureTestingModule({
      declarations: [QuarterFilterComponent],
      providers: [
        { provide: Router,
          useValue: mockRouter },
        { provide: ActivatedRoute,
          useValue: mockActivatedRoute },
        { provide: QuarterService,
          useValue: mockQuarterService },
        { provide: RefreshDataService,
          useValue: {} }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(QuarterFilterComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.detectChanges();

    expect(component)
      .toBeTruthy();
  });

  describe('Reactive State (Signals & Effects)', () => {
    it('should read currentQuarterId from query params', () => {
      (getValueFromQuery as jest.Mock).mockReturnValue([5]);
      mockActivatedRoute.queryParams.next({ quarter: '5' });

      fixture.detectChanges();

      expect(component.currentQuarterId())
        .toBe(5);
    });

    it('should emit the correct quarter label when a valid quarter ID is present in the route', () => {
      (getValueFromQuery as jest.Mock).mockReturnValue([7]);
      mockActivatedRoute.queryParams.next({ quarter: '7' });

      const emitSpy = jest.spyOn(component.quarterLabel$, 'emit');

      fixture.detectChanges();

      expect(emitSpy)
        .toHaveBeenCalledWith('Q3 - 2025');
    });

    it('should NOT emit a label if the ID does not match any quarters', () => {
      (getValueFromQuery as jest.Mock).mockReturnValue(['999']);
      mockActivatedRoute.queryParams.next({ quarter: '999' });

      const emitSpy = jest.spyOn(component.quarterLabel$, 'emit');

      fixture.detectChanges();

      expect(emitSpy)
        .toHaveBeenCalledWith('');
    });
  });

  describe('User Interactions', () => {
    it('should navigate and merge query params when changeDisplayedQuarter is called', () => {
      fixture.detectChanges();

      component.changeDisplayedQuarter(2);

      expect(mockRouter.navigate)
        .toHaveBeenCalledWith([], {
          queryParams: { quarter: 2 },
          queryParamsHandling: 'merge'
        });
    });
  });
});
