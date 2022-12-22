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
import * as objectivesData from '../../shared/testing/mock-data/objectives.json';
import * as teamsData from '../../shared/testing/mock-data/teams.json';
import { Overview } from '../../shared/services/overview.service';

describe('TeamDetailComponent', () => {
  let componentTeamDetails: TeamDetailComponent;
  let fixtureTeamDetails: ComponentFixture<TeamDetailComponent>;

  let overview: Overview = {
    team: {
      id: 1,
      name: 'Puzzle ITC',
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
        created: '01.01.2022',
      },
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
        created: '01.01.2022',
      },
    ],
  };

  let objectiveList: Observable<Objective[]> = of(objectivesData.objectives);

  let team: Team = teamsData.teams[0];

  const mockObjectiveService = {
    getObjectivesOfTeam: jest.fn(),
  };

  beforeEach(() => {
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
      providers: [],
    }).compileComponents();

    fixtureTeamDetails = TestBed.createComponent(TeamDetailComponent);
    componentTeamDetails = fixtureTeamDetails.componentInstance;
    componentTeamDetails.overview = overview;
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
    ).toEqual('Team 1 Objectives');
  });

  test('should create 3 hr when having 1 team with 3 objectives', () => {
    expect(
      fixtureTeamDetails.nativeElement.querySelectorAll('hr').length
    ).toEqual(3);
  });
});
