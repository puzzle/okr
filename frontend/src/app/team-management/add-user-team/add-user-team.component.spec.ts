import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddUserTeamComponent } from './add-user-team.component';
import { TeamService } from '../../services/team.service';
import { team1, team2, team3, testUser } from '../../shared/test-data';
import { of } from 'rxjs';

describe('AddUserTeamComponent', () => {
  let component: AddUserTeamComponent;
  let fixture: ComponentFixture<AddUserTeamComponent>;
  const team1Copy = { ...team1 };
  const team2Copy = { ...team2 };
  const team3Copy = { ...team3 };

  const teamServiceMock = {
    getAllTeams: jest.fn()
  };

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      declarations: [AddUserTeamComponent],
      providers: [{ provide: TeamService,
        useValue: teamServiceMock }]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddUserTeamComponent);
    component = fixture.componentInstance;

    teamServiceMock.getAllTeams.mockReturnValue(of([team1Copy,
      team2Copy,
      team3Copy]));
    component.currentTeams$ = of(testUser.userTeamList);

    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should filter selectableAdminTeams correctly', (done) => {
    team1Copy.isWriteable = true;
    team2Copy.isWriteable = true;
    team3Copy.isWriteable = false;
    component.ngOnInit();
    component.selectableAdminTeams$!.subscribe((teams) => {
      expect(teams.length)
        .toBe(1);
      expect(teams[0].id)
        .toBe(team2Copy.id);
      done();
    });
  });

  it('should filter allAdminTeams correctly', (done) => {
    team1Copy.isWriteable = true;
    team2Copy.isWriteable = true;
    team3Copy.isWriteable = false;
    component.ngOnInit();
    component.allAdminTeams$!.subscribe((teams) => {
      expect(teams.length)
        .toBe(2);
      expect(teams[0].id)
        .toBe(team1Copy.id);
      expect(teams[1].id)
        .toBe(team2Copy.id);
      expect(component.isAddButtonVisible(teams))
        .toBeTruthy();
      done();
    });
  });

  it('should create the userTeam when createUserTeam is called', () => {
    component.createUserTeam(team1Copy);
    expect(component.userTeam)
      .toStrictEqual({
        team: team1Copy,
        isTeamAdmin: false
      });
  });

  it('save should throw exception if userTeam is undefined', () => {
    expect(() => component.save())
      .toThrow('UserTeam should be defined here');
  });

  it('should emit addUserTeam event and set userTeam to undefined after save', (done) => {
    component.userTeam = testUser.userTeamList[0];
    component.addUserTeam.subscribe(() => {
      done();
    });
    component.save();
    expect(component.userTeam)
      .toBe(undefined);
  });

  it('should test showAddButton', () => {
    component.userTeam = testUser.userTeamList[0];
    expect(component.isAddButtonVisible(null))
      .toBeFalsy();
    expect(component.isAddButtonVisible([team1Copy,
      team2Copy]))
      .toBeFalsy();
    component.userTeam = undefined;
    expect(component.isAddButtonVisible([]))
      .toBeFalsy();
    expect(component.isAddButtonVisible([team1Copy,
      team2Copy]))
      .toBeTruthy();
  });

  it('should test addButtonDisabled', () => {
    expect(component.isAddButtonDisabled([team1Copy]))
      .toBeFalsy();
    expect(component.isAddButtonDisabled([]))
      .toBeTruthy();
    expect(component.isAddButtonDisabled(null))
      .toBeTruthy();
  });
});
