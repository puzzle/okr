import { ComponentFixture, fakeAsync, TestBed, tick, waitForAsync } from '@angular/core/testing';

import { TeamFilterComponent } from './team-filter.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { MatChipsModule } from '@angular/material/chips';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { MatChipListboxHarness } from '@angular/material/chips/testing';
import { TeamService } from '../shared/services/team.service';
import { RefreshDataService } from '../shared/services/refresh-data.service';
import { of, Subject } from 'rxjs';
import { team1, team2, team3, teamList } from '../shared/testData';
import { Router } from '@angular/router';
import { Team } from '../shared/types/model/Team';

const teamServiceMock = {
  getAllTeams: jest.fn(),
};

const refreshDataServiceMock = {
  reloadOverviewSubject: new Subject(),
  markDataRefresh: jest.fn,
};

describe('TeamFilterComponent', () => {
  let component: TeamFilterComponent;
  let fixture: ComponentFixture<TeamFilterComponent>;
  let loader: HarnessLoader;
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TeamFilterComponent],
      imports: [HttpClientTestingModule, RouterTestingModule, MatChipsModule],
      providers: [
        { provide: TeamService, useValue: teamServiceMock },
        { provide: RefreshDataService, useValue: refreshDataServiceMock },
      ],
    });
    fixture = TestBed.createComponent(TeamFilterComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
    teamServiceMock.getAllTeams.mockReturnValue(of(teamList));
    refreshDataServiceMock
      .markDataRefresh()
      .mockImplementation(() => refreshDataServiceMock.reloadOverviewSubject.next(null));
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should select all chips per default', waitForAsync(async () => {
    jest.spyOn(component.teams$, 'next');
    jest.spyOn(component, 'changeTeamFilterParams');
    component.ngOnInit();
    fixture.detectChanges();

    expect(component.teams$.next).toHaveBeenCalledWith(teamList);
    expect(component.teams$.next).toBeCalledTimes(1);
    expect(component.changeTeamFilterParams).toBeCalledTimes(1);
  }));

  it('should select the right chips', waitForAsync(async () => {
    const teamIds = teamList.map((e) => e.id).filter((e, i) => i < 2);
    jest.spyOn(component.teams$, 'next');
    jest.spyOn(component, 'changeTeamFilterParams');
    const routerHarness = await RouterTestingHarness.create();

    await routerHarness.navigateByUrl('/?teams=' + teamIds.join(','));

    component.ngOnInit();
    fixture.detectChanges();

    expect(component.teams$.next).toHaveBeenCalledWith(teamList);
    expect(component.teams$.next).toBeCalledTimes(1);
    expect(component.activeTeams).toStrictEqual(teamIds);
    expect(component.changeTeamFilterParams).toBeCalledTimes(1);
  }));

  it('activeTeams array should be empty when all teams are shown', waitForAsync(async () => {
    const teamIds = teamList.map((e) => e.id);
    jest.spyOn(component.teams$, 'next');
    jest.spyOn(component, 'changeTeamFilterParams');
    const routerHarness = await RouterTestingHarness.create();

    await routerHarness.navigateByUrl('/?teams=' + teamIds.join(','));

    component.ngOnInit();
    fixture.detectChanges();

    expect(component.teams$.next).toHaveBeenCalledWith(teamList);
    expect(component.teams$.next).toBeCalledTimes(1);
    expect(component.activeTeams.length).toBe(0);
    expect(component.changeTeamFilterParams).toBeCalledTimes(1);
  }));

  it('change filter params', waitForAsync(async () => {
    component.activeTeams = teamList.map((e) => e.id).filter((e, i) => i < 2);
    jest.spyOn(router, 'navigate');

    fixture.detectChanges();
    await component.changeTeamFilterParams();

    expect(router.navigate).toBeCalledTimes(1);
    expect(router.navigate).toHaveBeenCalledWith([], { queryParams: { teams: '1,2' } });
  }));

  it('change filter params and reload', fakeAsync(() => {
    component.activeTeams = teamList.map((e) => e.id).filter((e, i) => i < 2);
    jest.spyOn(component, 'changeTeamFilterParams');
    jest.spyOn(refreshDataServiceMock, 'markDataRefresh');

    fixture.detectChanges();
    component.changeTeamFilterParamsAndReload();
    tick(500);
    expect(component.changeTeamFilterParams).toBeCalledTimes(1);
    expect(refreshDataServiceMock.markDataRefresh).toBeCalledTimes(1);
  }));

  it.each([
    [[1], 2, [1, 2]],
    [[1, 2], 2, [1]],
    [[1, 2], 3, []],
    [[], 3, [1, 2]],
    [[3], 3, []],
  ])('toggle Selection', (activeTeams: number[], selected: number, expected: number[]) => {
    component.activeTeams = activeTeams;
    jest.spyOn(component, 'areAllTeamsShown');
    jest.spyOn(component, 'changeTeamFilterParamsAndReload');

    component.toggleSelection(selected);
    fixture.detectChanges();
    expect(component.changeTeamFilterParamsAndReload).toBeCalledTimes(1);
    expect(component.areAllTeamsShown).toBeCalledTimes(2);
    expect(component.activeTeams).toStrictEqual(expected);
  });

  it.each([
    [[1], false],
    [[1, 2], false],
    [[1, 2, 3], true],
    [[], true],
    [[1, 2, 4], false],
  ])('are all teams shown', (activeTeams: number[], expected: boolean) => {
    component.activeTeams = activeTeams;
    expect(component.areAllTeamsShown()).toBe(expected);
  });

  it.each([
    [[4, 5, 8], 17],
    [[], 0],
  ])('getAllObjectivesCount', (activeObjectivePerTeam: number[], sum: number) => {
    const teams = activeObjectivePerTeam.map((e) => {
      return { id: 1, name: '', activeObjectives: e } as Team;
    });
    expect(component.getAllObjectivesCount(teams)).toBe(sum);
  });

  it('select all', () => {
    component.activeTeams = teamList.map((e) => e.id).filter((e, i) => i < 2);
    jest.spyOn(component, 'changeTeamFilterParamsAndReload');
    component.selectAll();
    expect(component.changeTeamFilterParamsAndReload).toBeCalledTimes(1);
  });

  it('select all should do nothing', () => {
    component.activeTeams = [];
    jest.spyOn(component, 'changeTeamFilterParamsAndReload');
    component.selectAll();

    expect(component.changeTeamFilterParamsAndReload).toBeCalledTimes(0);
  });
});
