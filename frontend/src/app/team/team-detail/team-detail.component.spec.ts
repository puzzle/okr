import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamDetailComponent } from './team-detail.component';
import {
  Objective,
  ObjectiveService,
} from '../../shared/services/objective.service';
import { Observable, of } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Team } from '../../shared/services/team.service';
import { DashboardComponent } from '../../dashboard/dashboard.component';
import { CommonModule } from '@angular/common';
import { MatExpansionModule } from '@angular/material/expansion';
import { ObjectiveModule } from '../../objective/objective.module';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';

describe('TeamDetailComponent', () => {
  let componentTeamDetails: TeamDetailComponent;
  let fixtureTeamDetails: ComponentFixture<TeamDetailComponent>;

  let objectiveList: Observable<Objective[]> = of([
    {
      id: 1,
      teamName: 'Team Name',
      teamId: 1,
      title: 'Wir wollen unseren Umsatz verdoppeln',
      ownerId: 2,
      ownerFirstname: 'Ruedi',
      ownerLastname: 'Grochde',
      description: 'Sehr wichtig',
      progress: 5,
      quarterId: 1,
      quarterLabel: 'GJ 22/23-Q1',
      created: '01.01.2022',
    },
    {
      id: 2,
      teamName: 'Team Name',
      teamId: 1,
      title: 'Wir wollen unser Personal verdreifachen',
      ownerId: 2,
      ownerFirstname: 'Ruedi',
      ownerLastname: 'Grochde',
      description: 'Sehr wichtig',
      progress: 5,
      quarterId: 1,
      quarterLabel: 'GJ 22/23-Q1',
      created: '01.01.2022',
    },
  ]);

  let team: Team = {
    id: 1,
    name: 'Puzzle ITC',
  };

  const mockObjectiveService = {
    getObjectivesOfTeam: jest.fn(),
  };

  beforeEach(() => {
    mockObjectiveService.getObjectivesOfTeam.mockReturnValue(objectiveList);

    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        RouterTestingModule,
        CommonModule,
        MatExpansionModule,
        ObjectiveModule,
        NoopAnimationsModule,
      ],
      declarations: [TeamDetailComponent, DashboardComponent],
      providers: [
        { provide: ObjectiveService, useValue: mockObjectiveService },
      ],
    }).compileComponents();

    fixtureTeamDetails = TestBed.createComponent(TeamDetailComponent);
    componentTeamDetails = fixtureTeamDetails.componentInstance;
    componentTeamDetails.team = team;
    fixtureTeamDetails.detectChanges();
  });

  test('should create', () => {
    expect(componentTeamDetails).toBeTruthy();
  });

  test('should have 1 title from team', () => {
    expect(
      fixtureTeamDetails.nativeElement.querySelectorAll('h1').length
    ).toEqual(1);
  });

  test('should have title Puzzle ITC Objectives', () => {
    expect(
      fixtureTeamDetails.nativeElement.querySelector('h1').textContent
    ).toEqual('Puzzle ITC Objectives');
  });

  test('should create 2 hr when having 1 team with 1 objective', () => {
    expect(
      fixtureTeamDetails.nativeElement.querySelectorAll('hr').length
    ).toEqual(2);
  });
});
