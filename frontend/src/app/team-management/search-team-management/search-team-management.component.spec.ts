import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { SharedModule } from '../../shared/shared.module';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FilteredTeam, FilteredUser, SearchTeamManagementComponent } from './search-team-management.component';
import { TranslateTestingModule } from 'ngx-translate-testing';
// @ts-ignore
import * as de from '../../../assets/i18n/de.json';
import { MatIconModule } from '@angular/material/icon';
import { ReactiveFormsModule } from '@angular/forms';
import { TeamService } from '../../services/team.service';
import { UserService } from '../../services/user.service';
import { of } from 'rxjs';
import { Team } from '../../shared/types/model/team';
import { User } from '../../shared/types/model/user';
import { ActivatedRoute, ActivatedRouteSnapshot, Router } from '@angular/router';
import Spy = jasmine.Spy;

const teams: Team[] = [
  {
    id: 1,
    version: 1,
    name: 'ZZ the Puzzle Team - Keyword',
    isWriteable: false
  },
  {
    id: 2,
    version: 1,
    name: 'The Puzzle Team - Keyword',
    isWriteable: false
  },
  {
    id: 3,
    version: 1,
    name: 'Puzzle Team - No',
    isWriteable: false
  },
  {
    id: 4,
    version: 1,
    name: 'Team Ruedi - Noname',
    isWriteable: false
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

  let teamServiceMock: Partial<TeamService>;
  let userServiceMock: Partial<UserService>;
  let activatedRouteMock: Partial<ActivatedRoute>;
  let navigateSpy: Spy;
  beforeEach(async() => {
    jest.useFakeTimers();

    teamServiceMock = {
      getAllTeams: () => {
        return of(teams);
      }
    };

    userServiceMock = {
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
        TranslateTestingModule.withTranslations({
          de: de
        }),
        SharedModule
      ],
      declarations: [SearchTeamManagementComponent],
      providers: [{
        provide: TeamService,
        useValue: teamServiceMock
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
  });
  beforeEach(() => {
    navigateSpy = jest.spyOn(TestBed.inject(Router), 'navigateByUrl') as unknown as Spy;
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
    component.search.setValue('puz');
    fixture.detectChanges();
    jest.advanceTimersByTime(250);

    const filteredUsers = component.filteredUsers$.getValue();
    const filteredTeams = component.filteredTeams$.getValue();
    expect(filteredUsers.map(getDisplayValues))
      .toEqual(['Pete Parrot (parrot@puzzle.ch)',
        'Martin Käser (kaeser@puzzle.ch)']);

    expect(filteredTeams.map(getDisplayValues))
      .toEqual(['Puzzle Team - No',
        'The Puzzle Team - Keyword',
        'ZZ the Puzzle Team - Keyword']);
  });

  it('should generate html values correctly', () => {
    component.search.setValue('Ruedi');
    fixture.detectChanges();
    jest.advanceTimersByTime(250);

    const filteredUsers = component.filteredUsers$.getValue();
    const filteredTeams = component.filteredTeams$.getValue();

    expect(filteredUsers.map(getHTMLValues))
      .toEqual(['<strong>Ruedi</strong> Peters (rpeter@gmail.com)']);

    expect(filteredTeams.map(getHTMLValues))
      .toEqual(['Team <strong>Ruedi</strong> - Noname']);
  });

  it('should debounce inputs correctly', () => {
    component.search.setValue('Ruedi');
    fixture.detectChanges();

    jest.advanceTimersByTime(100); // After 100ms
    expect(component.filteredUsers$.getValue())
      .toHaveLength(0);
    expect(component.filteredTeams$.getValue())
      .toHaveLength(0);

    jest.advanceTimersByTime(110); // After 210ms
    expect(component.filteredUsers$.getValue()).not.toHaveLength(0);
    expect(component.filteredTeams$.getValue()).not.toHaveLength(0);
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
