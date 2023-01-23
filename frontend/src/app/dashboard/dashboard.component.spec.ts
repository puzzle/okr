import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardComponent } from './dashboard.component';
import { Team, TeamService } from '../shared/services/team.service';
import { Observable, of } from 'rxjs';
import { AppModule } from '../app.module';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { ReactiveFormsModule } from '@angular/forms';
import { Quarter, QuarterService } from '../shared/services/quarter.service';
import * as teamsData from '../shared/testing/mock-data/teams.json';
import * as quartersData from '../shared/testing/mock-data/quarters.json';
import * as overviewData from '../shared/testing/mock-data/overview.json';
import { Overview, OverviewService } from '../shared/services/overview.service';
import { MatSelectHarness } from '@angular/material/select/testing';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;

  let quarters: Observable<Quarter[]> = of(quartersData.quarters);

  let teams: Observable<Team[]> = of(teamsData.teams);
  let overview: Observable<Overview[]> = of(overviewData.overview);

  const teamServiceMock = {
    getTeams: jest.fn(),
  };

  const quarterServiceMock = {
    getQuarters: jest.fn(),
  };

  const overviewServiceMock = {
    getOverview: jest.fn(),
  };

  let loader: HarnessLoader;

  beforeEach(() => {
    overviewServiceMock.getOverview.mockReturnValue(overview);
    quarterServiceMock.getQuarters.mockReturnValue(quarters);
    teamServiceMock.getTeams.mockReturnValue(teams);

    TestBed.configureTestingModule({
      imports: [
        AppModule,
        NoopAnimationsModule,
        ReactiveFormsModule,
        HttpClientTestingModule,
      ],
      providers: [
        { provide: TeamService, useValue: teamServiceMock },
        { provide: QuarterService, useValue: quarterServiceMock },
        { provide: OverviewService, useValue: overviewServiceMock },
      ],
      declarations: [DashboardComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    loader = TestbedHarnessEnvironment.loader(fixture);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    quarterServiceMock.getQuarters.mockReset();
    teamServiceMock.getTeams.mockReset();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should display Objectives und Keyresults headline', () => {
    expect(fixture.nativeElement.querySelector('p').textContent).toEqual(
      'Objectives und Key Results'
    );
  });

  test('should display 2 dropdowns links', () => {
    expect(
      fixture.nativeElement.querySelectorAll('mat-form-field').length
    ).toEqual(2);
  });

  test('should display 3 team detail components when having 3 teams', () => {
    expect(
      fixture.nativeElement.querySelectorAll('app-team-detail').length
    ).toEqual(3);
  });

  test('should select quarter filter in dropdown and change filter', async () => {
    const select = await loader.getHarness(
      MatSelectHarness.with({
        selector: '#quarterDropdown',
      })
    );

    await select.open();
    const dropDownElements = await select.getOptions();
    const bugOption = await select.getOptions({ text: 'GJ 22/23-Q1' });
    await bugOption[0].click();

    expect(component.quarterFilter).toEqual(1);
    expect(dropDownElements.length).toEqual(5);
    expect(await select.getValueText()).toEqual('GJ 22/23-Q1');
    expect(await select.isDisabled()).toBeFalsy();
    expect(await select.isOpen()).toBeFalsy();
  });

  test('should select team filter in dropdown and change filter', async () => {
    const select = await loader.getHarness(
      MatSelectHarness.with({
        selector: '#teamDropdown',
      })
    );

    await select.open();
    const dropDownElements = await select.getOptions();
    const bugOptionFirstTeam = await select.getOptions({ text: 'Team 1' });
    const bugOptionSecondTeam = await select.getOptions({ text: 'Team 2' });
    await bugOptionFirstTeam[0].click();

    expect(await select.getValueText()).toEqual('Team 1');
    expect(component.teamFilter.length).toEqual(1);
    expect(component.teamFilter[0]).toEqual(1);

    await bugOptionSecondTeam[0].click();

    expect(await select.getValueText()).toEqual('Team 1, Team 2');
    expect(component.teamFilter.length).toEqual(2);
    expect(component.teamFilter[0]).toEqual(1);
    expect(component.teamFilter[1]).toEqual(2);

    expect(dropDownElements.length).toEqual(3);
    expect(await select.isDisabled()).toBeFalsy();
  });
});
