import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { TeamFilterComponent } from './team-filter.component';
import { RouterTestingModule } from '@angular/router/testing';
import { MatChipsModule } from '@angular/material/chips';
import { TeamStateService } from '../../../services/team.state.service';
import { RefreshDataService } from '../../../services/refresh-data.service';
import { Subject, of } from 'rxjs';
import { teamList, testUser } from '../../test-data';
import { ActivatedRoute, Router } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { UserService } from '../../../services/user.service';
import { extractTeamsFromUser } from '../../types/model/user';
import { ApplicationBannerComponent } from '../../custom/application-banner/application-banner.component';
import { Team } from '../../types/model/team';
import { TeamStatus } from '../../types/enums/team-status';
import { signal, WritableSignal } from '@angular/core';
import { BreakpointObserver } from '@angular/cdk/layout';

describe('TeamFilterComponent', () => {
  let component: TeamFilterComponent;
  let fixture: ComponentFixture<TeamFilterComponent>;
  let router: Router;
  let teamsSignal: WritableSignal<Team[]>;

  let activatedRouteMock: any;
  let teamStateServiceMock: any;
  let refreshDataServiceMock: any;
  let userServiceMock: any;
  let breakpointObserverMock: any;

  beforeEach(async() => {
    teamsSignal = signal<Team[]>([]);

    activatedRouteMock = {
      snapshot: {
        queryParams: {}
      }
    };

    teamStateServiceMock = {
      getTeams: jest.fn()
        .mockReturnValue(teamsSignal),
      reload: jest.fn()
    };

    refreshDataServiceMock = {
      reloadOverviewSubject: new Subject<null>(),
      teamFilterReady: new Subject<void>(),
      markDataRefresh: jest.fn()
    };

    userServiceMock = {
      getCurrentUser: jest.fn()
        .mockReturnValue(testUser)
    };

    breakpointObserverMock = {
      observe: jest.fn()
        .mockReturnValue(of({ matches: false }))
    };

    await TestBed.configureTestingModule({
      declarations: [TeamFilterComponent,
        ApplicationBannerComponent],
      imports: [RouterTestingModule,
        MatChipsModule,
        MatIconModule],
      providers: [
        { provide: TeamStateService,
          useValue: teamStateServiceMock },
        { provide: RefreshDataService,
          useValue: refreshDataServiceMock },
        { provide: UserService,
          useValue: userServiceMock },
        { provide: ActivatedRoute,
          useValue: activatedRouteMock },
        { provide: BreakpointObserver,
          useValue: breakpointObserverMock }
      ]
    })
      .compileComponents();
  });

  function setupComponent() {
    fixture = TestBed.createComponent(TeamFilterComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    jest.spyOn(router, 'navigate')
      .mockResolvedValue(true);
  }

  it('should create', () => {
    setupComponent();
    fixture.detectChanges();
    expect(component)
      .toBeTruthy();
  });

  it('should select all teams per default if no param is given', fakeAsync(() => {
    activatedRouteMock.snapshot.queryParams = {};
    setupComponent();
    jest.spyOn(component, 'changeTeamFilterParams');

    teamsSignal.set(teamList);
    fixture.detectChanges();
    tick();

    const expectedUserTeams = extractTeamsFromUser(testUser)
      .map((t) => t.id);
    expect(component.activeTeams())
      .toStrictEqual(expectedUserTeams);
    expect(component.changeTeamFilterParams)
      .toHaveBeenCalledTimes(1);
  }));

  it('should select the correct chips based on URL query parameters', fakeAsync(() => {
    const teamIds = teamList.map((e) => e.id)
      .slice(0, 2);
    activatedRouteMock.snapshot.queryParams = { teams: teamIds.join(',') };

    setupComponent();
    jest.spyOn(component, 'changeTeamFilterParams');

    teamsSignal.set(teamList);
    fixture.detectChanges();
    tick();

    expect(component.activeTeams())
      .toStrictEqual(teamIds);
    expect(component.changeTeamFilterParams)
      .toHaveBeenCalledTimes(1);
  }));

  it('should have all teams in activeTeams array when all teams are requested', fakeAsync(() => {
    const teamIds = teamList.map((e) => e.id);
    activatedRouteMock.snapshot.queryParams = { teams: teamIds.join(',') };

    setupComponent();
    jest.spyOn(component, 'changeTeamFilterParams');

    teamsSignal.set(teamList);
    fixture.detectChanges();
    tick();

    expect(component.activeTeams().length)
      .toBe(3);
    expect(component.changeTeamFilterParams)
      .toHaveBeenCalledTimes(1);
  }));

  it('should update route after updating filter', fakeAsync(() => {
    setupComponent();
    teamsSignal.set(teamList);

    fixture.detectChanges();
    tick();

    (router.navigate as jest.Mock).mockClear();

    component.activeTeams.set([8,
      5,
      10]);
    component.changeTeamFilterParams();
    tick();

    expect(router.navigate)
      .toHaveBeenCalledTimes(1);
    expect(router.navigate)
      .toHaveBeenCalledWith([], {
        queryParams: { teams: '8,5,10' },
        queryParamsHandling: 'merge'
      });
  }));

  it.each([
    [[1],
      2,
      [1,
        2]],
    [[1,
      2],
    2,
    [1]],
    [[1,
      2],
    3,
    [1,
      2,
      3]],
    [[],
      3,
      [3]],
    [[3],
      3,
      []]
  ])('should toggle Selection', (activeTeams: number[], selected: number, expected: number[]) => {
    setupComponent();
    teamsSignal.set(teamList);
    fixture.detectChanges();

    component.activeTeams.set(activeTeams);
    jest.spyOn(component, 'areAllTeamsShown');
    jest.spyOn(component, 'changeTeamFilterParams');

    component.toggleSelection(selected);

    expect(component.changeTeamFilterParams)
      .toHaveBeenCalledTimes(1);
    expect(component.areAllTeamsShown)
      .toHaveBeenCalledTimes(1);
    expect(component.activeTeams())
      .toStrictEqual(expected);
  });

  it.each([
    [[1],
      false],
    [[1,
      2],
    false],
    [[1,
      2,
      3],
    true],
    [[],
      false],
    [[1,
      2,
      4],
    false]
  ])('should correctly determine if all teams are shown', (activeTeams: number[], expected: boolean) => {
    setupComponent();
    teamsSignal.set(teamList);
    fixture.detectChanges();

    component.activeTeams.set(activeTeams);
    expect(component.areAllTeamsShown())
      .toBe(expected);
  });

  it.each([
    [[],
      [1,
        2,
        3]],
    [[1],
      [1,
        2,
        3]],
    [[1,
      2],
    [1,
      2,
      3]],
    [[1,
      2,
      3],
    []]
  ])('should correctly select all teams', (currentTeams: number[], expectedTeams: number[]) => {
    setupComponent();
    teamsSignal.set(teamList);
    fixture.detectChanges();

    component.activeTeams.set(currentTeams);
    jest.spyOn(component, 'changeTeamFilterParams');

    component.toggleAll();

    expect(component.changeTeamFilterParams)
      .toHaveBeenCalledTimes(1);
    expect(component.activeTeams())
      .toStrictEqual(expectedTeams);
  });

  it('should refresh teams on data refresh', fakeAsync(() => {
    setupComponent();
    teamsSignal.set(teamList);
    fixture.detectChanges();

    refreshDataServiceMock.reloadOverviewSubject.next(null);
    tick();

    expect(teamStateServiceMock.reload)
      .toHaveBeenCalledTimes(1);
  }));

  it('should use teams of user if no known teams are in url', fakeAsync(() => {
    activatedRouteMock.snapshot.queryParams = { teams: '654,478' };
    setupComponent();
    jest.spyOn(component, 'changeTeamFilterParams');

    teamsSignal.set(teamList);
    fixture.detectChanges();
    tick();

    const userTeamIds = extractTeamsFromUser(testUser)
      .map((team) => team.id);
    expect(component.activeTeams())
      .toStrictEqual(userTeamIds);
    expect(component.changeTeamFilterParams)
      .toHaveBeenCalledTimes(1);
  }));

  it.each([[[1,
    2,
    3],
  '1,2,3'],
  [[],
    null]])('should navigate after filter update', fakeAsync((currentTeams: number[], routingTeams: string | null) => {
    setupComponent();
    fixture.detectChanges();

    component.activeTeams.set(currentTeams);
    component.changeTeamFilterParams();
    tick();

    expect(router.navigate)
      .toHaveBeenCalledTimes(1);
    expect(router.navigate)
      .toHaveBeenCalledWith([], {
        queryParams: { teams: routingTeams },
        queryParamsHandling: 'merge'
      });
  }));

  it('should filter teams by toggled priority and then by name on mobile', fakeAsync(() => {
    breakpointObserverMock.observe.mockReturnValue(of({ matches: true }));
    setupComponent();

    const teams: Team[] = [
      { id: 1,
        version: 0,
        name: 'Team D',
        description: '',
        isWriteable: true,
        markedAsArchivedAt: null,
        status: TeamStatus.ACTIVE },
      { id: 2,
        version: 0,
        name: 'Team C',
        description: '',
        isWriteable: true,
        markedAsArchivedAt: null,
        status: TeamStatus.ACTIVE },
      { id: 3,
        version: 0,
        name: 'Team B',
        description: '',
        isWriteable: true,
        markedAsArchivedAt: null,
        status: TeamStatus.ACTIVE },
      { id: 4,
        version: 0,
        name: 'Team A',
        description: '',
        isWriteable: true,
        markedAsArchivedAt: null,
        status: TeamStatus.ACTIVE }
    ];

    teamsSignal.set(teams);
    fixture.detectChanges();

    component.activeTeams.set([3,
      4]);
    fixture.detectChanges();

    const sortedTeams = component.teams();

    expect(sortedTeams)
      .toEqual([
        { id: 4,
          version: 0,
          name: 'Team A',
          description: '',
          isWriteable: true,
          markedAsArchivedAt: null,
          status: TeamStatus.ACTIVE },
        { id: 3,
          version: 0,
          name: 'Team B',
          description: '',
          isWriteable: true,
          markedAsArchivedAt: null,
          status: TeamStatus.ACTIVE },
        { id: 2,
          version: 0,
          name: 'Team C',
          description: '',
          isWriteable: true,
          markedAsArchivedAt: null,
          status: TeamStatus.ACTIVE },
        { id: 1,
          version: 0,
          name: 'Team D',
          description: '',
          isWriteable: true,
          markedAsArchivedAt: null,
          status: TeamStatus.ACTIVE }
      ]);
  }));
});
