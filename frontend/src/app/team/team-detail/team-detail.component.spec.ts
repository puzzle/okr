import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamDetailComponent } from './team-detail.component';
import { Objective, ObjectiveService } from '../../services/objective.service';
import { Observable, of } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Team } from '../../services/team.service';
import { DashboardComponent } from '../../dashboard/dashboard.component';
import { CommonModule } from '@angular/common';

describe('TeamDetailComponent', () => {
  let componentTeamDetails: TeamDetailComponent;
  let fixtureTeamDetails: ComponentFixture<TeamDetailComponent>;

  const mockObjectiveService = {
    getObjectivesOfTeam: jest.fn(),
  };

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
      created: '01.01.2022',
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
      created: '01.01.2022',
    },
  ]);

  let team: Team = {
    id: 1,
    name: 'Puzzle ITC',
  };

  beforeEach(async () => {
    mockObjectiveService.getObjectivesOfTeam.mockReturnValue(objectiveList);

    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, CommonModule],
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

  it('should create', () => {
    expect(componentTeamDetails).toBeTruthy();
  });

  it('should have 1 title from team', () => {
    expect(
      fixtureTeamDetails.nativeElement.querySelectorAll('h1').length
    ).toEqual(1);
  });

  it('should have title Puzzle ITC Objectives', () => {
    expect(
      fixtureTeamDetails.nativeElement.querySelector('h1').textContent
    ).toEqual('Puzzle ITC Objectives');
  });

  it('should create 3 divs (1 Div with team name and 2 divs with objectives)', () => {
    expect(
      fixtureTeamDetails.nativeElement.querySelectorAll('div').length
    ).toEqual(3);
  });

  it('should create 2 hr when having 1 team with 1 objective', () => {
    expect(
      fixtureTeamDetails.nativeElement.querySelectorAll('hr').length
    ).toEqual(2);
  });
});
