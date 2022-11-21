import {ComponentFixture, TestBed} from '@angular/core/testing';

import {TeamDetailComponent} from './team-detail.component';
import {Objective, ObjectiveService} from './objective.service';
import {Observable, of} from 'rxjs';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {Team} from '../dashboard/team.service';
import {DashboardComponent} from '../dashboard/dashboard.component';
import {CommonModule} from '@angular/common';

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
    mockObjectiveService = jasmine.createSpyObj(['getObjectivesOfTeam']);
    mockObjectiveService.getObjectivesOfTeam.and.returnValue(objectiveList);

    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, CommonModule],
      declarations: [TeamDetailComponent, DashboardComponent],
      providers: [{provide: ObjectiveService, useValue: mockObjectiveService}]
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

  it('should create 6 divs (3 Divs with team names and 6 divs with objectives)', () => {
    expect(
      fixtureTeamDetails.nativeElement.querySelectorAll('div').length
    ).toEqual(9);
  });

  it('should have Puzzle ITC Ziele at the top', () => {
    expect(
      fixtureTeamDetails.nativeElement.querySelector('h1').textContent
    ).toEqual("Puzzle ITC Ziele");
  });

  it('should have dev/java Ziele at the end', () => {
    expect(
      fixtureTeamDetails.nativeElement.querySelectorAll('h1')[2].textContent
    ).toEqual("dev/java Ziele");
  });

  it('should create 6 hr when having 3 teams with 2 objectives', () => {
    expect(
      fixtureTeamDetails.nativeElement.querySelectorAll('hr').length
    ).toEqual(6);
  });
});
