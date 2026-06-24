import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BehaviorSubject } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { TeamListComponent } from './team-list.component';
import { Team } from '../../shared/types/model/team';
import { signal } from '@angular/core';
import { ALL_TEAMS_STATE } from '../../services/team-state.tokens';

describe('TeamListComponent', () => {
  let component: TeamListComponent;
  let fixture: ComponentFixture<TeamListComponent>;

  const paramMapSubject = new BehaviorSubject<{ get: (key: string) => any }>({
    get: (key: string) => '1'
  });

  const teamStateServiceMock = {
    getTeams: jest.fn()
      .mockReturnValue(signal([])),
    loadTeams: jest.fn()
  };

  const activatedRouteMock = {
    paramMap: paramMapSubject.asObservable()
  };

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      declarations: [TeamListComponent],
      providers: [{
        provide: ALL_TEAMS_STATE, // <-- Update this token
        useValue: teamStateServiceMock
      },
      {
        provide: ActivatedRoute,
        useValue: activatedRouteMock
      }]
    })
      .compileComponents();
  });

  beforeEach(() => {
    paramMapSubject.next({ get: (key: string) => '1' });

    fixture = TestBed.createComponent(TeamListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should set selected teamid when param is present', () => {
    expect(component.selectedTeamId)
      .toBe(1);
  });

  it('should set selected teamid to undefined when param is absent', () => {
    paramMapSubject.next({ get: (key: string) => undefined });

    expect(component.selectedTeamId)
      .toBe(undefined);
  });

  it('should sort teams correctly: active first (alphabetically), then archived (alphabetically)', () => {
    const activeZebra = { id: 1,
      name: 'Zebra',
      markedAsArchivedAt: null } as Team;
    const activeAlpha = { id: 2,
      name: 'Alpha',
      markedAsArchivedAt: null } as Team;
    const archivedYellow = { id: 3,
      name: 'Yellow',
      markedAsArchivedAt: new Date() } as Team;
    const archivedBeta = { id: 4,
      name: 'Beta',
      markedAsArchivedAt: new Date() } as Team;

    const unsortedTeams = [
      archivedYellow,
      activeZebra,
      archivedBeta,
      activeAlpha
    ];

    teamStateServiceMock.getTeams.mockReturnValue(signal(unsortedTeams));

    const fixture = TestBed.createComponent(TeamListComponent);
    const component = fixture.componentInstance;

    fixture.detectChanges();

    expect(component.teams()
      .map((t) => t.name))
      .toEqual([
        'Alpha',
        'Zebra',
        'Beta',
        'Yellow'
      ]);
  });
});
