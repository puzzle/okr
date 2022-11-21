import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamDetailComponent } from './team-detail.component';
import { Objective } from './objective.service';
import { Observable, of } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Team } from '../dashboard/team.service';
import { DashboardComponent } from '../dashboard/dashboard.component';
import { CommonModule } from '@angular/common';

describe('TeamDetailComponent', () => {
  let componentTeamDetails: TeamDetailComponent;
  let fixtureTeamDetails: ComponentFixture<TeamDetailComponent>;
  let mockObjectiveService;

  let objectiveList: Observable<Objective[]> = of([
    {
      id: 1,
      title: 'Wir wollen unseren Umsatz verdoppeln',
      ownerId: 2,
      ownerFirstname: 'Ruedi',
      ownerLastname: 'Grochde',
      description: 'Sehr wichtig',
      progress: 5,
      quarterId: 1,
      quarterNumber: 3,
      quarterYear: 2022,
    },
    {
      id: 2,
      title: 'Wir wollen unser Personal verdreifachen',
      ownerId: 2,
      ownerFirstname: 'Ruedi',
      ownerLastname: 'Grochde',
      description: 'Sehr wichtig',
      progress: 5,
      quarterId: 1,
      quarterNumber: 3,
      quarterYear: 2022,
    },
  ]);

  let teamsList: Observable<Team[]> = of([
    {
      id: 1,
      name: 'Puzzle ITC',
    },
    {
      id: 2,
      name: 'dev/ruby',
    },
    {
      id: 3,
      name: 'dev/java',
    },
  ]);

  beforeEach(async () => {
    mockObjectiveService = jasmine.createSpyObj([
      'getObjectivesOfTeam',
      'getTeams',
    ]);
    mockObjectiveService.getObjectivesOfTeam.and.returnValue(of(objectiveList));
    mockObjectiveService.getTeams.and.returnValue(of(teamsList));

    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, CommonModule],
      declarations: [TeamDetailComponent, DashboardComponent],
    }).compileComponents();

    fixtureTeamDetails = TestBed.createComponent(TeamDetailComponent);
    componentTeamDetails = fixtureTeamDetails.componentInstance;
    componentTeamDetails.teams = teamsList;
    fixtureTeamDetails.detectChanges();
  });

  it('should create', () => {
    expect(componentTeamDetails).toBeTruthy();
  });

  it('should create 3 teams', () => {
    expect(
      fixtureTeamDetails.nativeElement.querySelectorAll('h1').length
    ).toEqual(3);
  });
});
