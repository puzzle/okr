import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddUserTeamComponent } from './add-user-team.component';
import { TeamService } from '../../services/team.service';
import { team1, team2, team3, testUser } from '../../shared/testData';
import { of } from 'rxjs';

describe('AddUserTeamComponent', () => {
  let component: AddUserTeamComponent;
  let fixture: ComponentFixture<AddUserTeamComponent>;
  let teamService: TeamService;

  const team1Copy = { ...team1 };
  const team2Copy = { ...team2 };
  const team3Copy = { ...team3 };

  const teamServiceMock = {
    getAllTeams: jest.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddUserTeamComponent],
      providers: [{ provide: TeamService, useValue: teamServiceMock }],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddUserTeamComponent);
    component = fixture.componentInstance;
    teamService = TestBed.inject(TeamService);

    teamServiceMock.getAllTeams.mockReturnValue(of([team1Copy, team2Copy, team3Copy]));
    component.currentTeams$ = of(testUser.userTeamList);

    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should filter adminTeams correctly', (done) => {
    team1Copy.writeable = true;
    team2Copy.writeable = true;
    team3Copy.writeable = false;
    component.ngOnInit();
    component.adminTeams$!.subscribe((teams) => {
      expect(teams.length).toBe(1);
      expect(teams[0].id).toBe(team2Copy.id);
      done();
    });
  });

  it('createUserTeam should create the userTeam', () => {
    component.createUserTeam(team1Copy);
    expect(component.userTeam).toStrictEqual({
      team: team1Copy,
      isTeamAdmin: false,
    });
  });

  it('save should throw exception if userTeam is undefined', () => {
    expect(() => component.save()).toThrowError('UserTeam should be defined here');
  });

  it('save should emit addUserTeam event and set userTeam to undefined', (done) => {
    component.userTeam = testUser.userTeamList[0];
    component.addUserTeam.subscribe(() => {
      done();
    });
    component.save();
    expect(component.userTeam).toBe(undefined);
  });
});
