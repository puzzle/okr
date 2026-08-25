import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { SharedModule } from '../../shared/shared.module';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FilteredTeam, FilteredUser, SearchTeamManagementComponent } from './search-team-management.component';
// @ts-ignore
import * as de from '../../../assets/i18n/de.json';
import { MatIconModule } from '@angular/material/icon';
import { ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { of } from 'rxjs';
import { Team } from '../../shared/types/model/team';
import { User } from '../../shared/types/model/user';
import { ActivatedRoute, ActivatedRouteSnapshot, Router } from '@angular/router';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TeamStatus } from '../../shared/types/enums/team-status';
import { ALL_TEAMS_STATE } from '../../services/team-state.tokens';
import { signal } from '@angular/core';

const teams: Team[] = [
  {
    id: 1,
    version: 1,
    name: 'ZZ the Puzzle Team - Keyword',
    description: 'Description of ZZ the Puzzle Team',
    isWriteable: false,
    markedAsArchivedAt: null,
    status: TeamStatus.ACTIVE
  },
  {
    id: 2,
    version: 1,
    name: 'The Puzzle Team - Keyword',
    description: 'Description of The Puzzle Team',
    isWriteable: false,
    markedAsArchivedAt: null,
    status: TeamStatus.ACTIVE
  },
  {
    id: 3,
    version: 1,
    name: 'Puzzle Team - No',
    description: 'Description of Puzzle Team',
    isWriteable: false,
    markedAsArchivedAt: null,
    status: TeamStatus.ACTIVE
  },
  {
    id: 4,
    version: 1,
    name: 'Team Ruedi - Noname',
    description: 'Description of Team Ruedi',
    isWriteable: false,
    markedAsArchivedAt: null,
    status: TeamStatus.ACTIVE
  }
];

const users: User[] = [{
  id: 2,
  firstName: 'Pete',
  lastName: 'Parrot',
  email: 'parrot@puzzle.ch',
  userTeamList: [],
  isOkrChampion: false
},
{
  id: 1,
  firstName: 'Martin',
  lastName: 'Käser',
  email: 'kaeser@puzzle.ch',
  userTeamList: [],
  isOkrChampion: false
},
{
  id: 3,
  firstName: 'Ruedi',
  lastName: 'Peters',
  email: 'rpeter@gmail.com',
  userTeamList: [],
  isOkrChampion: false
}];

describe('SearchTeamManagementComponent', () => {
  let component: SearchTeamManagementComponent;
  let fixture: ComponentFixture<SearchTeamManagementComponent>;

  let activatedRouteMock: Partial<ActivatedRoute>;
  let navigateSpy: jest.SpyInstance;
  beforeEach(async() => {
    jest.useFakeTimers();

    const teamStateServiceMock = {
      getTeams: () => {
        return signal(teams);
      }
    };

    const userServiceMock = {
      getUsers: () => {
        return of(users);
      }
    };

    activatedRouteMock = {
      snapshot: {
        params: {}
      } as unknown as ActivatedRouteSnapshot
    };

    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        RouterTestingModule.withRoutes([]),
        BrowserAnimationsModule,
        MatAutocompleteModule,
        ReactiveFormsModule,
        MatInputModule,
        MatIconModule,
        TranslateModule.forRoot({
          loader: {
            provide: TranslateLoader,
            useValue: {
              getTranslation: () => of(de)
            }
          }
        }),
        SharedModule
      ],
      declarations: [SearchTeamManagementComponent],
      providers: [{
        provide: ALL_TEAMS_STATE,
        useValue: teamStateServiceMock
      },
      {
        provide: UserService,
        useValue: userServiceMock
      },
      {
        provide: ActivatedRoute,
        useValue: activatedRouteMock
      }]
    })
      .compileComponents();

    fixture = TestBed.createComponent(SearchTeamManagementComponent);
    component = fixture.componentInstance;
  });
  beforeEach(() => {
    navigateSpy = jest.spyOn(TestBed.inject(Router), 'navigateByUrl')
      .mockResolvedValue(true);
    fixture = TestBed.createComponent(SearchTeamManagementComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  afterEach(() => {
    jest.clearAllTimers();
  });

  const getDisplayValues = (value: FilteredUser | FilteredTeam) => value.displayValue;
  const getHTMLValues = (value: FilteredUser | FilteredTeam) => value.htmlValue;

  it('should filter out entries and order them according to the position in which the search value occurs', () => {
    fixture.detectChanges();
    component.search.setValue('puz');

    jest.advanceTimersByTime(250);

    const filteredUsers = component.filteredUsers();
    const filteredTeams = component.filteredTeams();

    expect(filteredUsers.map(getDisplayValues))
      .toEqual(['Pete Parrot (parrot@puzzle.ch)',
        'Martin Käser (kaeser@puzzle.ch)']);

    expect(filteredTeams.map(getDisplayValues))
      .toEqual(['Puzzle Team - No',
        'The Puzzle Team - Keyword',
        'ZZ the Puzzle Team - Keyword']);
  });

  it('should generate html values correctly', () => {
    fixture.detectChanges();

    component.search.setValue('Ruedi');

    jest.advanceTimersByTime(250);

    const filteredUsers = component.filteredUsers();
    const filteredTeams = component.filteredTeams();

    expect(filteredUsers.map(getHTMLValues))
      .toEqual(['<strong>Ruedi</strong> Peters (rpeter@gmail.com)']);

    expect(filteredTeams.map(getHTMLValues))
      .toEqual(['Team <strong>Ruedi</strong> - Noname']);
  });

  it('should debounce inputs correctly', () => {
    fixture.detectChanges();
    component.search.setValue('Ruedi');

    jest.advanceTimersByTime(100); // After 100ms

    expect(component.filteredUsers())
      .toHaveLength(0);
    expect(component.filteredTeams())
      .toHaveLength(0);

    jest.advanceTimersByTime(110); // After 210ms total

    expect(component.filteredUsers()).not.toHaveLength(0);
    expect(component.filteredTeams()).not.toHaveLength(0);
  });

  it('should stay on current team page when a user is selected', () => {
    activatedRouteMock!.snapshot!.params['teamId'] = '42';

    component.selectUser(users[0]);

    expect(navigateSpy)
      .toHaveBeenCalledWith('/team-management/42/details/member/2');
  });

  it('should stay on current root page when a user is selected', () => {
    activatedRouteMock!.snapshot!.params = {};

    component.selectUser(users[0]);

    expect(navigateSpy)
      .toHaveBeenCalledWith('/team-management/details/member/2');
  });

  it('should switch to teams page when selected', () => {
    component.selectTeam(teams[0]);

    expect(navigateSpy)
      .toHaveBeenCalledWith('/team-management/1');
  });
});
