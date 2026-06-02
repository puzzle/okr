import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { TeamService } from '../../services/team.service';
import { TeamListComponent } from './team-list.component';
import { Team } from '../../shared/types/model/team';

describe('TeamListComponent', () => {
  let component: TeamListComponent;
  let fixture: ComponentFixture<TeamListComponent>;
  const paramTeamId = 1;

  const teamServiceMock = {
    getAllTeams: jest.fn()
      .mockReturnValue(of())
  };

  const activatedRouteMock: any = {
    paramMap: of({
      get: () => paramTeamId
    })
  };

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      declarations: [TeamListComponent],
      providers: [{ provide: TeamService,
        useValue: teamServiceMock },
      { provide: ActivatedRoute,
        useValue: activatedRouteMock }]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TeamListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should set selected teamid', () => {
    component.ngOnInit();
    expect(component.selectedTeamId)
      .toBe(paramTeamId);
  });

  it('should set selected teamid', () => {
    activatedRouteMock.paramMap = of({
      get: () => undefined
    });
    component.ngOnInit();
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

    teamServiceMock.getAllTeams.mockReturnValue(of(unsortedTeams));

    const customFixture = TestBed.createComponent(TeamListComponent);
    const customComponent = customFixture.componentInstance;

    customComponent.teams$.subscribe((sortedTeams) => {
      expect(sortedTeams.map((t) => t.name))
        .toEqual([
          'Alpha', // 1st: Active, starts with A
          'Zebra', // 2nd: Active, starts with Z
          'Beta', // 3rd: Archived, starts with B
          'Yellow' // 4th: Archived, starts with Y
        ]);
    });
  });
});
