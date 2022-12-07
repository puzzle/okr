import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamListComponent } from './team-list.component';
import { of } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TeamService } from '../team.service';
import { By } from '@angular/platform-browser';

describe('TeamListComponent', () => {
  let component: TeamListComponent;
  let fixture: ComponentFixture<TeamListComponent>;

  const teamServiceMock = {
    getTeams: jest.fn(),
  };

  beforeEach(() => {
    teamServiceMock.getTeams.mockReturnValue(
      of([
        { id: 1, name: 'Team1' },
        { id: 2, name: 'Team2' },
      ])
    );

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [{ provide: TeamService, useValue: teamServiceMock }],
      declarations: [TeamListComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TeamListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should display a headline with right text', () => {
    expect(
      fixture.nativeElement.querySelector('.headline-large').textContent
    ).toEqual('Teams');
  });

  test('should display two teams', () => {
    const teamRows = fixture.debugElement.queryAll(By.css('.team-row'));
    expect(teamRows.length).toEqual(2);
  });

  test('should have right name on teams', () => {
    const teamRows = fixture.debugElement.queryAll(By.css('.team-row'));
    expect(teamRows[0].nativeElement.querySelector('p').textContent).toEqual(
      'Team1'
    );
    expect(teamRows[1].nativeElement.querySelector('p').textContent).toEqual(
      'Team2'
    );
  });

  test('should have two edit buttons', () => {
    const editButtons = fixture.debugElement.queryAll(By.css('.edit-icon'));
    expect(editButtons.length).toEqual(2);
  });

  test('should have two delete buttons', () => {
    const editButtons = fixture.debugElement.queryAll(By.css('.delete-icon'));
    expect(editButtons.length).toEqual(2);
  });

  test('should have two lines under the teams', () => {
    const dividers = fixture.debugElement.queryAll(By.css('.divider'));
    expect(dividers.length).toEqual(2);
  });
});
