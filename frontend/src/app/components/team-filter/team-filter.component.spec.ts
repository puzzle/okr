import { ComponentFixture, fakeAsync, TestBed, waitForAsync } from '@angular/core/testing';

import { TeamFilterComponent } from './team-filter.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { MatChipsModule } from '@angular/material/chips';
import { TeamService } from '../../services/team.service';
import { RefreshDataService } from '../../services/refresh-data.service';
import { BehaviorSubject, of, Subject } from 'rxjs';
import { team1, team2, team3, teamList, testUser } from '../../shared/test-data';
import { Router } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { UserService } from '../../services/user.service';
import { ApplicationBannerComponent } from '../application-banner/application-banner.component';

const teamServiceMock = {
  getAllTeams: jest.fn()
};

const refreshDataServiceMock = {
  reloadOverviewSubject: new Subject(),
  teamFilterReady: new Subject(),
  markDataRefresh: jest.fn
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
      providers: [{ provide: TeamService,
        useValue: teamServiceMock },
      { provide: RefreshDataService,
        useValue: refreshDataServiceMock },
      { provide: UserService,
        useValue: userServiceMock }]
    });
    fixture = TestBed.createComponent(TeamFilterComponent);
    component = fixture.componentInstance;
    teamServiceMock.getAllTeams.mockReturnValue(of(teamList));
    refreshDataServiceMock
      .markDataRefresh()
      .mockImplementation(() => refreshDataServiceMock.reloadOverviewSubject.next(null));
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
    teamServiceMock.getAllTeams.mockReturnValue(of([team2,
      team1]));
    fixture.detectChanges();
    expect(component.teams$.value)
      .toStrictEqual(teamList);
    refreshDataServiceMock.reloadOverviewSubject.next(null);
    fixture.detectChanges();
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
      .toStrictEqual(testUser.teamList
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
      .toStrictEqual(testUser.teamList
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
      .toHaveBeenCalledWith([], { queryParams: { teams: routingTeams } });
  });

  it('should filter teams by toggled priority and then by name', async() => {
    const teams = [
      { id: 1,
        version: 0,
        name: 'Team D',
        isWriteable: true },
      { id: 2,
        version: 0,
        name: 'Team C',
        isWriteable: true },
      { id: 3,
        version: 0,
        name: 'Team B',
        isWriteable: true },
      { id: 4,
        version: 0,
        name: 'Team A',
        isWriteable: true }
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
          isWriteable: true },
        { id: 3,
          version: 0,
          name: 'Team B',
          isWriteable: true },
        { id: 2,
          version: 0,
          name: 'Team C',
          isWriteable: true },
        { id: 1,
          version: 0,
          name: 'Team D',
          isWriteable: true }
      ]);
  });
});
