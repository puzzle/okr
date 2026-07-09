import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TeamFilterComponent } from './team-filter.component';
import { ActivatedRoute, Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { BreakpointObserver } from '@angular/cdk/layout';
import { TeamStateService } from '../../../services/team.state.service';
import { RefreshDataService } from '../../../services/refresh-data.service';
import { signal, WritableSignal } from '@angular/core';

jest.mock('../../common', () => ({
  areEqual: (a: any[], b: any[]) => a.length === b.length && a.every((val) => b.includes(val)),
  optionalReplaceWithNulls: jest.fn((obj) => obj)
}));

const mockTeamsData = [{ id: 1,
  name: 'Zebra Team' },
{ id: 2,
  name: 'Apple Team' },
{ id: 3,
  name: 'Banana Team' }];

describe('TeamFilterComponent', () => {
  let component: TeamFilterComponent;
  let fixture: ComponentFixture<TeamFilterComponent>;

  let mockRouter: { navigate: jest.Mock };
  let queryParamsSubject: BehaviorSubject<any>;
  let breakpointSubject: BehaviorSubject<any>;
  let mockTeamsSignal: WritableSignal<any[]>;

  beforeEach(async() => {
    mockRouter = { navigate: jest.fn() };
    queryParamsSubject = new BehaviorSubject<any>({});
    breakpointSubject = new BehaviorSubject<any>({ matches: false });
    mockTeamsSignal = signal(mockTeamsData);

    await TestBed.configureTestingModule({
      declarations: [TeamFilterComponent],
      providers: [
        { provide: Router,
          useValue: mockRouter },
        { provide: ActivatedRoute,
          useValue: { queryParams: queryParamsSubject } },
        { provide: BreakpointObserver,
          useValue: { observe: jest.fn()
            .mockReturnValue(breakpointSubject) } },
        { provide: TeamStateService,
          useValue: { getTeams: jest.fn()
            .mockReturnValue(mockTeamsSignal) } },
        { provide: RefreshDataService,
          useValue: {} }
      ]
    })
      .compileComponents();
  });

  const setupComponent = () => {
    fixture = TestBed.createComponent(TeamFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  };

  it('should create', () => {
    setupComponent();
    expect(component)
      .toBeTruthy();
  });

  describe('Reactive Signals (activeTeams & isMobile)', () => {
    it('should resolve empty activeTeams when no param is provided', () => {
      queryParamsSubject.next({});
      setupComponent();

      expect(component.activeTeams())
        .toEqual([]);
    });

    it('should parse activeTeams correctly from a comma-separated query string', () => {
      queryParamsSubject.next({ teams: '1,3' });
      setupComponent();

      expect(component.activeTeams())
        .toEqual([1,
          3]);
    });

    it('should parse activeTeams correctly if query param is already an array', () => {
      queryParamsSubject.next({ teams: ['2',
        '3'] });
      setupComponent();

      expect(component.activeTeams())
        .toEqual([2,
          3]);
    });

    it('should react to mobile breakpoint changes', () => {
      breakpointSubject.next({ matches: true });
      setupComponent();

      expect(component.isMobile())
        .toBe(true);
    });
  });

  describe('Computed teams() Signal', () => {
    it('should return raw teams unsorted on desktop devices', () => {
      breakpointSubject.next({ matches: false });
      setupComponent();

      // Original order: Zebra, Apple, Banana
      expect(component.teams()
        .map((t) => t.id))
        .toEqual([1,
          2,
          3]);
    });

    it('should sort active teams first, then alphabetically by name on mobile devices', () => {
      breakpointSubject.next({ matches: true });
      queryParamsSubject.next({ teams: '3' });
      setupComponent();

      const sortedIds = component.teams()
        .map((t) => t.id);

      // Expected: Banana Team (Active: 3), Apple Team (Alpha: 2), Zebra Team (Alpha: 1)
      expect(sortedIds)
        .toEqual([3,
          2,
          1]);
    });
  });

  describe('User Actions & Routing', () => {
    beforeEach(() => {
      setupComponent();
    });

    it('should navigate with merged params when changeTeamFilterParams is called', () => {
      component.changeTeamFilterParams([1,
        2]);

      expect(mockRouter.navigate)
        .toHaveBeenCalledWith([], {
          queryParams: { teams: '1,2' },
          queryParamsHandling: 'merge'
        });
    });

    it('toggleSelection: should isolate selection if all teams are currently shown', () => {
      queryParamsSubject.next({ teams: '1,2,3' });
      fixture.detectChanges();

      component.toggleSelection(2);

      expect(mockRouter.navigate)
        .toHaveBeenCalledWith([], expect.objectContaining({
          queryParams: { teams: '2' }
        }));
    });

    it('toggleSelection: should remove team if already selected', () => {
      queryParamsSubject.next({ teams: '1,2' });
      fixture.detectChanges();

      component.toggleSelection(1);

      expect(mockRouter.navigate)
        .toHaveBeenCalledWith([], expect.objectContaining({
          queryParams: { teams: '2' }
        }));
    });

    it('toggleSelection: should add team if not selected', () => {
      queryParamsSubject.next({ teams: '1' });
      fixture.detectChanges();

      component.toggleSelection(2);

      expect(mockRouter.navigate)
        .toHaveBeenCalledWith([], expect.objectContaining({
          queryParams: { teams: '1,2' }
        }));
    });

    it('toggleSelection: should NOT remove a team if doing so violates minTeams', () => {
      component.minTeams = 1;
      queryParamsSubject.next({ teams: '2' });
      fixture.detectChanges();

      component.toggleSelection(2);

      expect(mockRouter.navigate).not.toHaveBeenCalled();
    });

    it('toggleAll: should select all teams if not all are currently shown', () => {
      queryParamsSubject.next({ teams: '1' });
      fixture.detectChanges();

      component.toggleAll();

      expect(mockRouter.navigate)
        .toHaveBeenCalledWith([], expect.objectContaining({
          queryParams: { teams: '1,2,3' }
        }));
    });

    it('toggleAll: should clear all teams if all are currently shown', () => {
      queryParamsSubject.next({ teams: '1,2,3' });
      fixture.detectChanges();

      component.toggleAll();

      expect(mockRouter.navigate)
        .toHaveBeenCalledWith([], expect.objectContaining({
          queryParams: { teams: '' }
        }));
    });

    it('toggleAll: should do nothing if all are shown but minTeams > 0', () => {
      component.minTeams = 1;
      queryParamsSubject.next({ teams: '1,2,3' });
      fixture.detectChanges();

      component.toggleAll();

      expect(mockRouter.navigate).not.toHaveBeenCalled();
    });
  });
});
