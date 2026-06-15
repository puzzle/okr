import { ComponentFixture, fakeAsync, TestBed, waitForAsync } from '@angular/core/testing';
import { TeamFilterComponent } from './team-filter.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { MatChipsModule } from '@angular/material/chips';
import { TeamStateService } from '../../../services/team.state.service'; // Updated Import
import { RefreshDataService } from '../../../services/refresh-data.service';
import { BehaviorSubject, Subject } from 'rxjs';
import { team1, team2, team3, teamList, testUser } from '../../test-data';
import { Router } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { UserService } from '../../../services/user.service';
import { extractTeamsFromUser } from '../../types/model/user';
import { ApplicationBannerComponent } from '../../custom/application-banner/application-banner.component';
import { Team } from '../../types/model/team';
import { TeamStatus } from '../../types/enums/team-status';

// Reactive stream to simulate active state changes inside the mock
const teamsStateStream$ = new BehaviorSubject<Team[]>(teamList);

const teamStateServiceMock = {
  getTeams: jest.fn()
    .mockReturnValue(teamsStateStream$.asObservable()),
  loadTeams: jest.fn(),
  reload: jest.fn()
};

const refreshDataServiceMock = {
  reloadOverviewSubject: new Subject<null>(),
  teamFilterReady: new Subject<void>(),
  markDataRefresh: jest.fn()
};

const userServiceMock = {
  getCurrentUser: jest.fn()
};

describe('TeamFilterComponent', () => {
  let component: TeamFilterComponent;
  let fixture: ComponentFixture<TeamFilterComponent>;
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TeamFilterComponent,
        ApplicationBannerComponent],
      imports: [
        HttpClientTestingModule,
        RouterTestingModule,
        MatChipsModule,
        MatIconModule
      ],
      providers: [{ provide: TeamStateService,
        useValue: teamStateServiceMock }, // Updated Token
      { provide: RefreshDataService,
        useValue: refreshDataServiceMock },
      { provide: UserService,
        useValue: userServiceMock }]
    });

    fixture = TestBed.createComponent(TeamFilterComponent);
    component = fixture.componentInstance;

    // Reset stream data and configure mock implementations safely
    teamsStateStream$.next(teamList);
    refreshDataServiceMock.markDataRefresh.mockImplementation(() => {
      refreshDataServiceMock.reloadOverviewSubject.next(null);
    });

    router = TestBed.inject(Router);
    userServiceMock.getCurrentUser.mockReturnValue(testUser);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should select all chips per default', waitForAsync(async() => {
    jest.spyOn(component.teams$, 'next');
    jest.spyOn(component, 'changeTeamFilterParams');
    component.ngOnInit();
    fixture.detectChanges();

    expect(component.teams$.next)
      .toHaveBeenCalledWith(teamList);
    expect(component.teams$.next)
      .toHaveBeenCalledTimes(1);
    expect(component.changeTeamFilterParams)
      .toHaveBeenCalledTimes(1);
  }));

  it('should select the correct chips', waitForAsync(async() => {
    const teamIds = teamList.map((e) => e.id)
      .filter((e, i) => i < 2);
    jest.spyOn(component.teams$, 'next');
    jest.spyOn(component, 'changeTeamFilterParams');
    const routerHarness = await RouterTestingHarness.create();

    await routerHarness.navigateByUrl('/?teams=' + teamIds.join(','));

    component.ngOnInit();
    fixture.detectChanges();

    expect(component.teams$.next)
      .toHaveBeenCalledWith(teamList);
    expect(component.teams$.next)
      .toHaveBeenCalledTimes(1);
    expect(component.activeTeams)
      .toStrictEqual(teamIds);
    expect(component.changeTeamFilterParams)
      .toHaveBeenCalledTimes(1);
  }));

  it('should have all teams in activeTeams array when all teams are shown', waitForAsync(async() => {
    const teamIds = teamList.map((e) => e.id);
    jest.spyOn(component.teams$, 'next');
    jest.spyOn(component, 'changeTeamFilterParams');
    const routerHarness = await RouterTestingHarness.create();

    await routerHarness.navigateByUrl('/?teams=' + teamIds.join(','));

    component.ngOnInit();
    fixture.detectChanges();

    expect(component.teams$.next)
      .toHaveBeenCalledWith(teamList);
    expect(component.teams$.next)
      .toHaveBeenCalledTimes(1);
    expect(component.activeTeams.length)
      .toBe(3);
    expect(component.changeTeamFilterParams)
      .toHaveBeenCalledTimes(1);
  }));

  it('should update route after updating filter', fakeAsync(async() => {
    component.activeTeams = teamList.map((e) => e.id)
      .filter((e, i) => i < 2);
    const routerHarness = await RouterTestingHarness.create();
    jest.spyOn(component, 'changeTeamFilterParams');
    jest.spyOn(refreshDataServiceMock, 'markDataRefresh');

    component.activeTeams = [8,
      5,
      10];
    fixture.detectChanges();
    await component.changeTeamFilterParams();
    routerHarness.detectChanges();

    expect(component.changeTeamFilterParams)
      .toHaveBeenCalledTimes(1);
    expect(router.url)
      .toBe('/?teams=8,5,10');
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
    component.activeTeams = activeTeams;
    jest.spyOn(component, 'areAllTeamsShown');
    jest.spyOn(component, 'changeTeamFilterParams');

    component.toggleSelection(selected);
    fixture.detectChanges();

    expect(component.changeTeamFilterParams)
      .toHaveBeenCalledTimes(1);
    expect(component.areAllTeamsShown)
      .toHaveBeenCalledTimes(1);
    expect(component.activeTeams)
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
    component.activeTeams = activeTeams;
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
    component.activeTeams = currentTeams;
    jest.spyOn(component, 'changeTeamFilterParams');
    component.toggleAll();

    expect(component.changeTeamFilterParams)
      .toHaveBeenCalledTimes(1);
    expect(component.activeTeams)
      .toStrictEqual(expectedTeams);
  });

  it('should refresh teams on data refresh', () => {
    component.ngOnInit();
    component.activeTeams = [team2.id,
      team3.id];
    fixture.detectChanges();
    expect(component.teams$.value)
      .toStrictEqual(teamList);

    // Update the in-memory state stream values
    teamsStateStream$.next([team2,
      team1]);

    // Explicitly toggle state visibility context flags to align with calculation paths
    (component as any).isInitialLoad = true;
    fixture.detectChanges();

    refreshDataServiceMock.reloadOverviewSubject.next(null);
    fixture.detectChanges();

    expect(teamStateServiceMock.reload)
      .toHaveBeenCalled();
    expect(component.teams$.value)
      .toStrictEqual([team2,
        team1]);
    expect(component.activeTeams)
      .toStrictEqual([team1.id]);
  });

  it('should use teams of user if no known teams are in url', async() => {
    const teamIds = [654,
      478];
    jest.spyOn(component.teams$, 'next');
    jest.spyOn(component, 'changeTeamFilterParams');
    const routerHarness = await RouterTestingHarness.create();

    await routerHarness.navigateByUrl('/?teams=' + teamIds.join(','));

    component.ngOnInit();
    fixture.detectChanges();

    expect(component.activeTeams.length)
      .toBe(1);
    expect(component.activeTeams)
      .toStrictEqual(extractTeamsFromUser(testUser)
        .map((team) => team.id));
    expect(component.changeTeamFilterParams)
      .toHaveBeenCalledTimes(1);
  });

  it('should use teams of user if no teams are in url', async() => {
    jest.spyOn(component.teams$, 'next');
    jest.spyOn(component, 'changeTeamFilterParams');
    const routerHarness = await RouterTestingHarness.create();

    await routerHarness.navigateByUrl('');

    component.ngOnInit();
    fixture.detectChanges();

    expect(component.activeTeams.length)
      .toBe(1);
    expect(component.activeTeams)
      .toStrictEqual(extractTeamsFromUser(testUser)
        .map((team) => team.id));
    expect(component.changeTeamFilterParams)
      .toHaveBeenCalledTimes(1);
  });

  it.each([[[1,
    2,
    3],
  '1,2,3'],
  [[],
    null]])('should navigate after filter update', async(currentTeams: number[], routingTeams: string | null) => {
    component.activeTeams = currentTeams;
    jest.spyOn(router, 'navigate');

    fixture.detectChanges();
    await component.changeTeamFilterParams();

    expect(router.navigate)
      .toHaveBeenCalledTimes(1);
    expect(router.navigate)
      .toHaveBeenCalledWith([], {
        queryParams: { teams: routingTeams },
        queryParamsHandling: 'merge'
      });
  });

  it('should filter teams by toggled priority and then by name', async() => {
    const teams: Team[] = [
      { id: 1,
        version: 0,
        name: 'Team D',
        description: 'Team Delta',
        isWriteable: true,
        markedAsArchivedAt: null,
        status: TeamStatus.ACTIVE },
      { id: 2,
        version: 0,
        name: 'Team C',
        description: 'Team Charlie',
        isWriteable: true,
        markedAsArchivedAt: null,
        status: TeamStatus.ACTIVE },
      { id: 3,
        version: 0,
        name: 'Team B',
        description: 'Team Bravo',
        isWriteable: true,
        markedAsArchivedAt: null,
        status: TeamStatus.ACTIVE },
      { id: 4,
        version: 0,
        name: 'Team A',
        description: 'Team Alpha',
        isWriteable: true,
        markedAsArchivedAt: null,
        status: TeamStatus.ACTIVE }
    ];

    component.teams$ = new BehaviorSubject(teams);
    component.activeTeams = [3,
      4];

    const sortedTeams = component.sortTeamsToggledPriority();

    expect(sortedTeams)
      .toEqual([
        { id: 4,
          version: 0,
          name: 'Team A',
          description: 'Team Alpha',
          isWriteable: true,
          markedAsArchivedAt: null,
          status: TeamStatus.ACTIVE },
        { id: 3,
          version: 0,
          name: 'Team B',
          description: 'Team Bravo',
          isWriteable: true,
          markedAsArchivedAt: null,
          status: TeamStatus.ACTIVE },
        { id: 2,
          version: 0,
          name: 'Team C',
          description: 'Team Charlie',
          isWriteable: true,
          markedAsArchivedAt: null,
          status: TeamStatus.ACTIVE },
        { id: 1,
          version: 0,
          name: 'Team D',
          description: 'Team Delta',
          isWriteable: true,
          markedAsArchivedAt: null,
          status: TeamStatus.ACTIVE }
      ]);
  });

  it('should load teams for specific quarter when quarter query param is present', async() => {
    teamStateServiceMock.loadTeams.mockClear();
    const routerHarness = await RouterTestingHarness.create();

    await routerHarness.navigateByUrl('/?quarter=42');

    expect(teamStateServiceMock.loadTeams)
      .toHaveBeenCalledTimes(1);
    expect(teamStateServiceMock.loadTeams)
      .toHaveBeenCalledWith({ quarterId: 42 });
  });

  it('should load teams with undefined quarter when quarter query param is absent', async() => {
    const routerHarness = await RouterTestingHarness.create();

    await routerHarness.navigateByUrl('/?quarter=42');

    teamStateServiceMock.loadTeams.mockClear();

    await routerHarness.navigateByUrl('/');

    expect(teamStateServiceMock.loadTeams)
      .toHaveBeenCalledTimes(1);
    expect(teamStateServiceMock.loadTeams)
      .toHaveBeenCalledWith({ quarterId: undefined });
  });
});
