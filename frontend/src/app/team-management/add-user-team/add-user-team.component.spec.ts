import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddUserTeamComponent } from './add-user-team.component';
import { team1, team2, team3, testUser } from '../../shared/test-data';
import { ALL_TEAMS_STATE } from '../../services/team-state.tokens';
import { signal, WritableSignal } from '@angular/core';
import { Team } from '../../shared/types/model/team';

describe('AddUserTeamComponent', () => {
  let component: AddUserTeamComponent;
  let fixture: ComponentFixture<AddUserTeamComponent>;
  let teamsSignal: WritableSignal<Team[]>;

  const team1Copy = { ...team1 };
  const team2Copy = { ...team2 };
  const team3Copy = { ...team3 };

  let teamStateServiceMock: any;

  beforeEach(async() => {
    teamsSignal = signal<Team[]>([]);

    teamStateServiceMock = {
      getTeams: jest.fn()
        .mockReturnValue(teamsSignal)
    };

    await TestBed.configureTestingModule({
      declarations: [AddUserTeamComponent],
      providers: [{ provide: ALL_TEAMS_STATE,
        useValue: teamStateServiceMock }]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddUserTeamComponent);
    component = fixture.componentInstance;

    teamsSignal.set([team1Copy,
      team2Copy,
      team3Copy]);

    fixture.componentRef.setInput('currentTeams', testUser.userTeamList);

    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should filter selectableAdminTeams correctly', () => {
    team1Copy.isWriteable = true;
    team2Copy.isWriteable = true;
    team3Copy.isWriteable = false;
    teamsSignal.set([team1Copy,
      team2Copy,
      team3Copy]);
    fixture.detectChanges();

    const teams = component.selectableAdminTeams();

    expect(teams.length)
      .toBe(1);
    expect(teams[0].id)
      .toBe(team2Copy.id);
  });

  it('should filter allAdminTeams correctly', () => {
    team1Copy.isWriteable = true;
    team2Copy.isWriteable = true;
    team3Copy.isWriteable = false;
    teamsSignal.set([team1Copy,
      team2Copy,
      team3Copy]);
    fixture.detectChanges();

    const teams = component.allAdminTeams();

    expect(teams.length)
      .toBe(2);
    expect(teams[0].id)
      .toBe(team1Copy.id);
    expect(teams[1].id)
      .toBe(team2Copy.id);
    expect(component.isAddButtonVisible(teams))
      .toBeTruthy();
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

  it('should emit addUserTeam event and set userTeam to undefined after save', () => {
    component.userTeam = testUser.userTeamList[0];
    jest.spyOn(component.addUserTeam, 'emit');

    component.save();

    expect(component.addUserTeam.emit)
      .toHaveBeenCalledWith(testUser.userTeamList[0]);
    expect(component.userTeam)
      .toBeUndefined();
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
