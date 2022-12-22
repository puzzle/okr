import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardComponent } from './dashboard.component';
import { Team, TeamService } from '../shared/services/team.service';
import { By } from '@angular/platform-browser';
import { Observable, of } from 'rxjs';
import { AppModule } from '../app.module';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { ReactiveFormsModule } from '@angular/forms';
import { Quarter, QuarterService } from '../shared/services/quarter.service';
import * as teamsData from '../shared/testing/mock-data/teams.json';
import * as quartersData from '../shared/testing/mock-data/quarters.json';
import { OverviewService } from '../shared/services/overview.service';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;

  let quarters: Observable<Quarter[]> = of(quartersData.quarters);

  let teams: Observable<Team[]> = of(teamsData.teams);

  const teamServiceMock = {
    getTeams: jest.fn(),
  };

  const quarterServiceMock = {
    getQuarters: jest.fn(),
  };

  const overviewServiceMock = {
    getOverview: jest.fn(),
  };

  beforeEach(() => {
    overviewServiceMock.getOverview.mockReturnValue(
      of([
        {
          team: {
            id: 1,
            name: 'Team 1',
          },
          objectives: [
            {
              id: 1,
              title: 'Objective 1',
              ownerId: 1,
              ownerFirstname: 'Alice',
              ownerLastname: 'Wunderland',
              teamId: 1,
              teamName: 'Team 1',
              quarterId: 1,
              quarterLabel: 'GJ 22/23-Q1',
              description: 'This is the description of Objective 1',
              progress: 20,
            },
          ],
        },
        {
          team: {
            id: 2,
            name: 'Team 2',
          },
          objectives: [
            {
              id: 2,
              title: 'Objective 2',
              ownerId: 1,
              ownerFirstname: 'Alice',
              ownerLastname: 'Wunderland',
              teamId: 1,
              teamName: 'Team 1',
              quarterId: 1,
              quarterLabel: 'GJ 22/23-Q1',
              description: 'This is the description of Objective 1',
              progress: 20,
            },
          ],
        },
      ])
    );
    quarterServiceMock.getQuarters.mockReturnValue(quarters);
    teamServiceMock.getTeams.mockReturnValue(teams);

    TestBed.configureTestingModule({
      imports: [AppModule, NoopAnimationsModule, ReactiveFormsModule],
      providers: [
        { provide: TeamService, useValue: teamServiceMock },
        { provide: QuarterService, useValue: quarterServiceMock },
        { provide: OverviewService, useValue: overviewServiceMock },
      ],
      declarations: [DashboardComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
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
      'Objectives und Keyresults'
    );
  });

  test('should display 2 dropdowns links', () => {
    expect(
      fixture.nativeElement.querySelectorAll('mat-form-field').length
    ).toEqual(2);
  });

  test('should display 5 items in quarter dropdown', () => {
    fixture.debugElement
      .queryAll(By.css('mat-select'))[1]
      .nativeElement.click();
    fixture.detectChanges();
    const options = fixture.debugElement.queryAll(By.css('mat-option'));

    expect(options.length).toEqual(5);
  });

  test('should display 3 teams in team dropdown', () => {
    fixture.debugElement.query(By.css('mat-select')).nativeElement.click();
    fixture.detectChanges();
    const options = fixture.debugElement.queryAll(By.css('mat-option'));

    expect(options.length).toEqual(3);
  });

  test('should display 3 team detail components when having 3 teams', () => {
    expect(
      fixture.nativeElement.querySelectorAll('app-team-detail').length
    ).toEqual(3);
  });
});
