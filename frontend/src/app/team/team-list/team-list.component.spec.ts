import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamListComponent } from './team-list.component';
import { Observable, of } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Team, TeamService } from '../../shared/services/team.service';
import { By } from '@angular/platform-browser';
import { MatDividerModule } from '@angular/material/divider';
import { RouterTestingModule } from '@angular/router/testing';
import * as teamsData from '../../shared/testing/mock-data/teams.json';

describe('TeamListComponent', () => {
  let component: TeamListComponent;
  let fixture: ComponentFixture<TeamListComponent>;

  let teamList: Observable<Team[]> = of(teamsData.teams);

  const teamServiceMock = {
    getTeams: jest.fn(),
  };

  beforeEach(() => {
    teamServiceMock.getTeams.mockReturnValue(teamList);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, MatDividerModule, RouterTestingModule],
      providers: [{ provide: TeamService, useValue: teamServiceMock }],
      declarations: [TeamListComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TeamListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    teamServiceMock.getTeams.mockReset();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should display a headline with right text', () => {
    expect(
      fixture.nativeElement.querySelector('.headline-large').textContent
    ).toEqual('Teams');
  });

  test('should have create team button with right text', () => {
    expect(
      fixture.nativeElement.querySelector('.create-button').textContent
    ).toEqual(' Team erstellen ');
  });

  test('should display three teams', () => {
    const teamRows = fixture.debugElement.queryAll(By.css('.team-row'));
    expect(teamRows.length).toEqual(3);
  });

  test('should have right name on teams', () => {
    const teamRows = fixture.debugElement.queryAll(By.css('.team-row'));
    expect(teamRows[0].nativeElement.querySelector('p').textContent).toEqual(
      'Team 1'
    );
    expect(teamRows[1].nativeElement.querySelector('p').textContent).toEqual(
      'Team 2'
    );
  });

  test('should have three edit buttons', () => {
    const editButtons = fixture.debugElement.queryAll(By.css('.edit-icon'));
    expect(editButtons.length).toEqual(3);
  });

  test('should have three delete buttons', () => {
    const editButtons = fixture.debugElement.queryAll(By.css('.delete-icon'));
    expect(editButtons.length).toEqual(3);
  });

  test('should have three lines under the teams', () => {
    const dividers = fixture.debugElement.queryAll(By.css('.divider'));
    expect(dividers.length).toEqual(3);
  });
});
